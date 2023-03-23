package com.example.hw_comand.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * A class for sending reports.
 * @author Andrei Klimov
 * @version 1.0.0
 */
@Entity
@Table(name = "report_data")
public class ReportData {

    /** "ID" field */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_data_id")
    private Long id;

    /** "id Chat" field */
    @Column(name = "chat_id")
    private Long chatId;

    /** "Description" field */
    @Column(name = "description")
    private String description;


    /** "Report Days" field */
    @Column(name = "report_days")
    private long reportDays;

    /** "File Path" field */
    @Column(name = "file_path")
    private String filePath;

    /** "Data" field */
    @Column(name = "data")
    @Lob
    private byte[] data;


    /** "Last Message" field */
    @Column(name = "last_message")
    private Date lastMessage;

    /**
     * Constructor - creating a new object.
     * @see ReportData
     */
    public ReportData() {
    }

    /**
     * Constructor - creating a new object with certain values.
     * @param chatId
     * @param data
     */
    public ReportData(Long chatId, byte[] data) {
        this.chatId = chatId;
        this.data = data;
    }

    /**
     * Constructor - creating a new object with certain values.
     * @param chatId
     * @param description
     * @param reportDays
     * @param filePath
     * @param data
     * @param lastMessage
     */
    public ReportData(Long chatId, String description, long reportDays, String filePath, byte[] data, Date lastMessage) {
        this.chatId = chatId;
        this.description = description;
        this.reportDays = reportDays;
        this.filePath = filePath;
        this.data = data;
        this.lastMessage = lastMessage;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getReportDays() {
        return reportDays;
    }

    public void setReportDays(long reportDays) {
        this.reportDays = reportDays;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportData that = (ReportData) o;
        return reportDays == that.reportDays && Objects.equals(id, that.id) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, reportDays);
    }

    @Override
    public String toString() {
        return "ReportData{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", description='" + description + '\'' +
                ", reportDays=" + reportDays +
                ", filePath='" + filePath +
                ", data=" + Arrays.toString(data) +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
