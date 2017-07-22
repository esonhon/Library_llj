package com.common.library.llj.okhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import okhttp3.Request;

/**
 * 处理子线程的返回结果
 * Created by liulj on 15/9/2.
 */
public abstract class AbstractResponseHandler<T> implements ResponseHandler<T>, ResponseDispatch<T> {
    private static final String TAG = AbstractResponseHandler.class.getSimpleName();
    private boolean useSynchronousMode;//true 使用同步
    private Handler handler;
    private Looper looper = null;

    private static final int TOAST_MESSAGE                 = -1;
    private static final int START_MESSAGE                 = 0;
    private static final int SUCCESS_MESSAGE               = 1;
    private static final int SUCCESS_BY_OTHER_CODE_MESSAGE = 2;
    private static final int FAILURE_MESSAGE               = 3;
    private static final int FINISH_MESSAGE                = 4;
    protected Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    AbstractResponseHandler() {
        this(null);
    }

    private AbstractResponseHandler(Looper looper) {
        this.looper = looper == null ? Looper.getMainLooper() : looper;
        setUseSynchronousMode(false);

    }

    @Override
    public final void sendToastMessage(String str) {
        sendMessage(obtainMessage(TOAST_MESSAGE, new Object[]{str}));
    }

    @Override
    public final void sendStartMessage(Request request) {
        sendMessage(obtainMessage(START_MESSAGE, new Object[]{request}));
    }

    @Override
    public final void sendSuccessMessage(T response) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{response}));
    }

    @Override
    public final void sendSuccessByOtherStatus(T response) {
        sendMessage(obtainMessage(SUCCESS_BY_OTHER_CODE_MESSAGE, new Object[]{response}));

    }

    @Override
    public final void sendFailureMessage(Request request, Exception e) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{request, e}));
    }

    @Override
    public final void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, new Object[]{FINISH_MESSAGE}));
    }

    @Override
    public final void handleMessage(Message message) {
        Object[] response;
        try {
            if (message != null) {
                switch (message.what) {
                    case TOAST_MESSAGE:
                        if (message.obj != null) {
                            response = (Object[]) message.obj;
                            onToast(response[0].toString());
                        }
                        break;
                    case START_MESSAGE:
                        if (message.obj != null) {
                            response = (Object[]) message.obj;
                            onStart((Request) response[0]);
                        }
                        break;
                    case SUCCESS_MESSAGE:
                        if (message.obj != null) {
                            response = (Object[]) message.obj;
                            onSuccess((T) response[0]);
                        }
                        break;
                    case SUCCESS_BY_OTHER_CODE_MESSAGE:
                        if (message.obj != null) {
                            response = (Object[]) message.obj;
                            onSuccessByOtherStatus((T) response[0]);
                        }
                        break;
                    case FAILURE_MESSAGE:
                        if (message.obj != null) {
                            response = (Object[]) message.obj;
                            onFailure((Request) response[0], (Exception) response[1]);
                        }
                    case FINISH_MESSAGE:
                        onFinish();
                        break;

                }
            }
        } catch (Throwable error) {
            throw new RuntimeException(error);
        }
    }


    private Message obtainMessage(int responseMessageId, Object responseMessageData) {
        return Message.obtain(handler, responseMessageId, responseMessageData);
    }

    @Override
    public boolean getUseSynchronousMode() {
        return useSynchronousMode;
    }

    @Override
    public void setUseSynchronousMode(boolean sync) {
        // A looper must be prepared before setting asynchronous mode.
        if (!sync && looper == null) {
            sync = true;
        }
        // If using asynchronous mode.
        //使用异步
        if (!sync && handler == null) {
            // Create a handler on current thread to submit tasks
            handler = new ResponderHandler(this, looper);
        } else if (sync && handler != null) {
            handler = null;
        }
        useSynchronousMode = sync;
    }

    /**
     * @param msg
     */
    protected final void sendMessage(Message msg) {
        if (getUseSynchronousMode() || handler == null) {
            handleMessage(msg);
        } else if (!Thread.currentThread().isInterrupted()) { // do not send messages if request has been cancelled
            handler.sendMessage(msg);
        }
    }

    /**
     *
     */
    private static class ResponderHandler extends Handler {
        private final AbstractResponseHandler mResponder;

        ResponderHandler(AbstractResponseHandler mResponder, Looper looper) {
            super(looper);
            this.mResponder = mResponder;
        }

        @Override
        public void handleMessage(Message msg) {
            mResponder.handleMessage(msg);
        }
    }
}