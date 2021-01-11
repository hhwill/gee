package com.geekcattle.model.msg;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "msg_short_info")
public class MsgShortInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "info_id")
    private String infoId;

    @Column(name = "info_time")
    private String infoTime;

    @Column(name = "info_type")
    private String infoType;

    @Column(name = "info_account_num")
    private String infoAccountNum;

    @Column(name = "info_amt")
    private BigDecimal infoAmt;

    @Column(name = "info_opp")
    private String infoOpp;

    @Column(name = "info_bank_name")
    private String infoBankName;

    @Column(name = "info_balance")
    private BigDecimal infoBalance;

    @Column(name = "info_sender")
    private String infoSender;

    @Column(name = "info_receive_key")
    private String infoReceiveKey;

    @Column(name = "data_status")
    private String dataStatus;

    @Column(name = "insert_time")
    private Date insertTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return info_id
     */
    public String getInfoId() {
        return infoId;
    }

    /**
     * @param infoId
     */
    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    /**
     * @return info_time
     */
    public String getInfoTime() {
        return infoTime;
    }

    /**
     * @param infoTime
     */
    public void setInfoTime(String infoTime) {
        this.infoTime = infoTime;
    }

    /**
     * @return info_type
     */
    public String getInfoType() {
        return infoType;
    }

    /**
     * @param infoType
     */
    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    /**
     * @return info_account_num
     */
    public String getInfoAccountNum() {
        return infoAccountNum;
    }

    /**
     * @param infoAccountNum
     */
    public void setInfoAccountNum(String infoAccountNum) {
        this.infoAccountNum = infoAccountNum;
    }

    /**
     * @return info_amt
     */
    public BigDecimal getInfoAmt() {
        return infoAmt;
    }

    /**
     * @param infoAmt
     */
    public void setInfoAmt(BigDecimal infoAmt) {
        this.infoAmt = infoAmt;
    }

    /**
     * @return info_opp
     */
    public String getInfoOpp() {
        return infoOpp;
    }

    /**
     * @param infoOpp
     */
    public void setInfoOpp(String infoOpp) {
        this.infoOpp = infoOpp;
    }

    /**
     * @return info_bank_name
     */
    public String getInfoBankName() {
        return infoBankName;
    }

    /**
     * @param infoBankName
     */
    public void setInfoBankName(String infoBankName) {
        this.infoBankName = infoBankName;
    }

    /**
     * @return info_balance
     */
    public BigDecimal getInfoBalance() {
        return infoBalance;
    }

    /**
     * @param infoBalance
     */
    public void setInfoBalance(BigDecimal infoBalance) {
        this.infoBalance = infoBalance;
    }

    /**
     * @return info_sender
     */
    public String getInfoSender() {
        return infoSender;
    }

    /**
     * @param infoSender
     */
    public void setInfoSender(String infoSender) {
        this.infoSender = infoSender;
    }

    /**
     * @return info_receive_key
     */
    public String getInfoReceiveKey() {
        return infoReceiveKey;
    }

    /**
     * @param infoReceiveKey
     */
    public void setInfoReceiveKey(String infoReceiveKey) {
        this.infoReceiveKey = infoReceiveKey;
    }

    /**
     * @return data_status
     */
    public String getDataStatus() {
        return dataStatus;
    }

    /**
     * @param dataStatus
     */
    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    /**
     * @return insert_time
     */
    public Date getInsertTime() {
        return insertTime;
    }

    /**
     * @param insertTime
     */
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}