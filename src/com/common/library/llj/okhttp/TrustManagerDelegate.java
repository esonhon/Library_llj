package com.common.library.llj.okhttp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * new created instance trust all certificates. you can call {@link #trustWhatSystemTrust()} and
 * {@link #trustSpecifiedCertificates(InputStream...)} to set your trust strategy. After any of
 * above method is called, this instance no longer accept any untrusted certificates.
 */
public final class TrustManagerDelegate implements X509TrustManager {

    private static final String TAG = "TrustManagerDelegate";

    private final List<X509TrustManager> mX509TrustManagerList = new ArrayList<>();
    private final List<X509Certificate>  mX509CertificateList  = new ArrayList<>();
    private       X509Certificate[]      mX509Certificates     = null;

    public SSLSocketFactory createSSLSocketFactory() {
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{this}, null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public TrustManagerDelegate trustWhatSystemTrust() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            addTrustManagers(trustManagerFactory.getTrustManagers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public TrustManagerDelegate trustSpecifiedCertificates(InputStream... certificateStreams) {
        try {
            final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);

            for (int i = 0; i < certificateStreams.length; ++i) {
                final InputStream certificateSteam = certificateStreams[i];
                if (certificateSteam != null) {
                    final String certificateAlias = Integer.toString(i);
                    keyStore.setCertificateEntry(certificateAlias,
                            certificateFactory.generateCertificate(certificateSteam));
                    try {
                        certificateSteam.close();
                    } catch (IOException ignore) {

                    }
                }
            }

            final TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            addTrustManagers(trustManagerFactory.getTrustManagers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private void addTrustManagers(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                final X509TrustManager tm = (X509TrustManager) trustManager;
                mX509TrustManagerList.add(tm);

                final X509Certificate[] certs = tm.getAcceptedIssuers();
                if (certs != null && certs.length > 0) {
                    mX509CertificateList.addAll(Arrays.asList(certs));
                    mX509Certificates = mX509CertificateList.toArray(
                            new X509Certificate[mX509CertificateList.size()]);
                }
            } else {
                Log.e(TAG, "addTrustManagers: trustManager not instance of X509TrustManager");
            }
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509TrustManager manager : mX509TrustManagerList) {
            try {
                manager.checkClientTrusted(chain, authType);
                return;
            } catch (CertificateException ignore) {
            }
        }
        if (mX509TrustManagerList.size() > 0) {
            throw new CertificateException();
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509TrustManager manager : mX509TrustManagerList) {
            try {
                manager.checkServerTrusted(chain, authType);
                return;
            } catch (CertificateException ignore) {
            }
        }
        if (mX509TrustManagerList.size() > 0) {
            throw new CertificateException();
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        if (mX509TrustManagerList.size() > 0) {
            return mX509Certificates;
        } else {
            return null;
        }
    }
}
