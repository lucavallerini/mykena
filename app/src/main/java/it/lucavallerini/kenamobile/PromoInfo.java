package it.lucavallerini.kenamobile;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Date;

public class PromoInfo extends BaseObservable {
    // Promo plan information
    private String promoName;
    private Date promoActivationDate;
    private Date promoTerminationDate;
    private String promoPrice;
    private String shortDescription;
    private String longDescription;

    private double dataHuman;
    private String dataUnit;
    private double dataByteUsed;
    private double dataByteTotal;
    private double dataGByteTotal;

    private double dataEuHuman;
    private String dataEuUnit;
    private double dataEuByteUsed;
    private double dataEuByteTotal;
    private double dataEuGByteTotal;

    private double callsHuman;
    private String callsUnit;
    private double callsSecsUsed;
    private double callsSecsTotal;
    private double callsMinTotal;

    private int callsPercentageRemaining;
    private int smsPercentageRemaining;
    private int dataPercentageRemaining;
    private int dataEuPercentageRemaining;

    @Bindable
    public int getCallsPercentageRemaining() {
        return callsPercentageRemaining;
    }

    void setCallsPercentageRemaining(int percentageRemaining) {
        callsPercentageRemaining = percentageRemaining;
        notifyPropertyChanged(BR.callsPercentageRemaining);
    }

    @Bindable
    public int getSmsPercentageRemaining() {
        return smsPercentageRemaining;
    }

    public void setSmsPercentageRemaining(int percentageRemaining) {
        smsPercentageRemaining = percentageRemaining;
        notifyPropertyChanged(BR.smsPercentageRemaining);
    }

    @Bindable
    public int getDataPercentageRemaining() {
        return dataPercentageRemaining;
    }

    void setDataPercentageRemaining(int percentageRemaining) {
        dataPercentageRemaining = percentageRemaining;
        notifyPropertyChanged(BR.dataEuPercentageRemaining);
    }

    @Bindable
    public int getDataEuPercentageRemaining() {
        return dataEuPercentageRemaining;
    }

    void setDataEuPercentageRemaining(int percentage) {
        dataEuPercentageRemaining = percentage;
        notifyPropertyChanged(BR.dataEuPercentageRemaining);
    }

    @Bindable
    public String getPromoName() {
        return promoName;
    }

    void setPromoName(String promoName) {
        this.promoName = promoName;
        notifyPropertyChanged(BR.promoName);
    }

    @Bindable
    public Date getPromoActivationDate() {
        return promoActivationDate;
    }

    void setPromoActivationDate(Date promoActivationDate) {
        this.promoActivationDate = promoActivationDate;
        notifyPropertyChanged(BR.promoActivationDate);
    }

    @Bindable
    public Date getPromoTerminationDate() {
        return promoTerminationDate;
    }

    void setPromoTerminationDate(Date promoTerminationDate) {
        this.promoTerminationDate = promoTerminationDate;
        notifyPropertyChanged(BR.promoTerminationDate);
    }

    @Bindable
    public String getPromoPrice() {
        return promoPrice;
    }

    void setPromoPrice(String promoPrice) {
        this.promoPrice = promoPrice;
        notifyPropertyChanged(BR.promoPrice);
    }

    @Bindable
    public String getShortDescription() {
        return shortDescription;
    }

    void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Bindable
    public String getLongDescription() {
        return longDescription;
    }

    void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @Bindable
    public double getDataHuman() {
        return dataHuman;
    }

    void setDataHuman(double dataHuman) {
        this.dataHuman = dataHuman;
    }

    @Bindable
    public String getDataUnit() {
        return dataUnit;
    }

    void setDataUnit(String dataUnit) {
        this.dataUnit = dataUnit;
    }

    public double getDataByteUsed() {
        return dataByteUsed;
    }

    void setDataByteUsed(double dataByteUsed) {
        this.dataByteUsed = dataByteUsed;
    }

    public double getDataByteTotal() {
        return dataByteTotal;
    }

    void setDataByteTotal(double dataByteTotal) {
        this.dataByteTotal = dataByteTotal;
    }

    @Bindable
    public double getDataEuHuman() {
        return dataEuHuman;
    }

    void setDataEuHuman(double dataEuHuman) {
        this.dataEuHuman = dataEuHuman;
    }

    @Bindable
    public String getDataEuUnit() {
        return dataEuUnit;
    }

    void setDataEuUnit(String dataEuUnit) {
        this.dataEuUnit = dataEuUnit;
    }

    public double getDataEuByteUsed() {
        return dataEuByteUsed;
    }

    void setDataEuByteUsed(double dataEuByteUsed) {
        this.dataEuByteUsed = dataEuByteUsed;
    }

    public double getDataEuByteTotal() {
        return dataEuByteTotal;
    }

    void setDataEuByteTotal(double dataEuByteTotal) {
        this.dataEuByteTotal = dataEuByteTotal;
    }

    @Bindable
    public double getCallsHuman() {
        return callsHuman;
    }

    void setCallsHuman(double callsHuman) {
        this.callsHuman = callsHuman;
    }

    @Bindable
    public double getDataGByteTotal() {
        return dataGByteTotal;
    }

    void setDataGByteTotal(double dataGByteTotal) {
        this.dataGByteTotal = dataGByteTotal;
    }

    @Bindable
    public double getDataEuGByteTotal() {
        return dataEuGByteTotal;
    }

    void setDataEuGByteTotal(double dataEuGByteTotal) {
        this.dataEuGByteTotal = dataEuGByteTotal;
    }

    @Bindable
    public double getCallsMinTotal() {
        return callsMinTotal;
    }

    void setCallsMinTotal(double callsMinTotal) {
        this.callsMinTotal = callsMinTotal;
    }

    @Bindable
    public String getCallsUnit() {
        return callsUnit;
    }

    void setCallsUnit(String callsUnit) {
        this.callsUnit = callsUnit;
    }

    public double getCallsSecsUsed() {
        return callsSecsUsed;
    }

    void setCallsSecsUsed(double callsSecsUsed) {
        this.callsSecsUsed = callsSecsUsed;
    }

    public double getCallsSecsTotal() {
        return callsSecsTotal;
    }

    void setCallsSecsTotal(double callsSecsTotal) {
        this.callsSecsTotal = callsSecsTotal;
    }
}
