package it.lucavallerini.kenamobile;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Date;

public class DashboardInfo extends BaseObservable {
    private double creditLeft;
    private String phoneNumber;
    private Date simActivationDate;
    private Date simTerminationDate;
    private String tariffPlan;

    @Bindable
    public double getCreditLeft() {
        return this.creditLeft;
    }

    void setCreditLeft(double creditLeft) {
        this.creditLeft = creditLeft;
        notifyPropertyChanged(BR.creditLeft);
    }

    @Bindable
    public String getTariffPlan() {
        return this.tariffPlan;
    }

    void setTariffPlan(String tariffPlan) {
        this.tariffPlan = tariffPlan;
        notifyPropertyChanged(BR.tariffPlan);
    }

    @Bindable
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);
    }

    @Bindable
    public Date getSimActivationDate() {
        return this.simActivationDate;
    }

    void setSimActivationDate(Date simActivationDate) {
        this.simActivationDate = simActivationDate;
        notifyPropertyChanged(BR.simActivationDate);
    }

    @Bindable
    public Date getSimTerminationDate() {
        return this.simTerminationDate;
    }

    void setSimTerminationDate(Date simTerminationDate) {
        this.simTerminationDate = simTerminationDate;
        notifyPropertyChanged(BR.simTerminationDate);
    }
}
