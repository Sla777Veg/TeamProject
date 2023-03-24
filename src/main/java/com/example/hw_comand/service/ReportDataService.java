
package com.example.hw_comand.service;

import com.example.hw_comand.exceptions.ReportDataNotFoundException;
import com.example.hw_comand.model.ReportData;
import com.example.hw_comand.repository.ReportDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * Class of ReportDataService.
 * @author Obukhov Damir
 * @version 1.0.0
 */
@Service
@Transactional
public class ReportDataService {


    private final ReportDataRepository reportRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReportDataService.class);

    public ReportDataService(ReportDataRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Method to upload ReportData.
     * @param personId
     * @param pictureFile
     * @param filePath
     * @param description
     * @param dateSendMessage
     * @throws IOException
     * @see ReportDataService
     */
    public void uploadReportData(Long personId, byte[] pictureFile, Path filePath, String description, Date dateSendMessage) throws IOException {
        logger.info("Was invoked method to uploadReportData");

        ReportData report = new ReportData();

        Optional<ReportData> lastReport = reportRepository.findFirstByChatIdOrderByLastMessageDesc(personId);

        report.setReportDays(lastReport.map(rep -> rep.getReportDays()+1).orElse(1L));
        report.setLastMessage(dateSendMessage);
        report.setFilePath(filePath.toString());
        report.setChatId(personId);
        report.setData(pictureFile);
        report.setDescription(description);
        this.reportRepository.save(report);
    }

    /**
     * Method to get a report by id.
     * @param id
     * @return {@link ReportDataRepository#findById(Object)}
     * @see ReportDataService
     * @exception ReportDataNotFoundException
     */
    public ReportData findById(Long id) {
        logger.info("Was invoked method to get a report by id={}", id);

        return this.reportRepository.findById(id)
                .orElseThrow(ReportDataNotFoundException::new);
    }

    /**
     * Method to get a report by chatId.
     * @param chatId
     * @return {@link ReportDataRepository#findByChatId(Long)}
     * @see ReportDataService
     */
    public Optional<ReportData> findByChatId(Long chatId) {
        logger.info("Was invoked method to get a report by chatId={}", chatId);

        return this.reportRepository.findByChatId(chatId);
    }

    /**
     * Method to findListByChatId a report by chat id.
     * @param chatId
     * @return {@link ReportDataRepository#findListByChatId(Long)}
     * @see ReportDataService
     */
    public Collection<ReportData> findListByChatId(Long chatId) {
        logger.info("Was invoked method to findListByChatId a report by chatId={}", chatId);

        if (chatId != null) {
            return this.reportRepository.findListByChatId(chatId);
        }
        return emptyList();
    }

    /**
     * Method to save a report.
     * @param report
     * @return {@link ReportDataRepository#findListByChatId(Long)}
     * @see ReportDataService
     */
    public ReportData save(ReportData report) {
        logger.info("Was invoked method to save a report");

        return this.reportRepository.save(report);
    }

    /**
     * Method to remove a report by id.
     * @param id
     * @see ReportDataService
     */
    public void remove(Long id) {
        logger.info("Was invoked method to remove a report by id={}", id);

        this.reportRepository.deleteById(id);
    }

    /**
     * Method to get all reports.
     * @return {@link ReportDataRepository#findAll()}
     * @see ReportDataService
     */
    public Collection<ReportData> getAll() {
        logger.info("Was invoked method to get all reports");

        return this.reportRepository.findAll();
    }

    /**
     * Method to get all reports.
     * @param pageNumber
     * @param pageSize
     * @return {@link ReportDataRepository#findAll()}
     * @see ReportDataService
     */
    public Collection<ReportData> getAllReports(Integer pageNumber, Integer pageSize) {
        logger.info("Was invoked method to get all reports");

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return this.reportRepository.findAll(pageRequest).getContent();
    }

    /**
     * Method to getExtensions.
     * @param fileName
     * @see ReportDataService
     */
    private String getExtensions(String fileName) {
        logger.info("Was invoked method to get Extensions");

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}