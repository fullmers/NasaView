package com.amiculous.nasaview.data;

public interface ApodCallback {
    void setCallState(ApodRepository.ApodCallState callState);
    void setApod(ApodEntity apodEntity);
}
