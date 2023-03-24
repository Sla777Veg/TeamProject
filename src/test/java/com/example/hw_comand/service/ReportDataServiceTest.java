package com.example.hw_comand.service;


import com.example.hw_comand.exceptions.ReportDataNotFoundException;
import com.example.hw_comand.model.*;

import com.example.hw_comand.repository.ReportDataRepository;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.mockito.ArgumentMatchers.any;



/**
 * Class for testing ReportDataService
 * @see ReportDataService
 * @see ReportDataRepository
 * @author Obukhov Damir
 */
@ExtendWith(MockitoExtension.class)
public class ReportDataServiceTest {

    @Mock
    private ReportDataRepository reportDataRepositoryMock;

    @InjectMocks
    private ReportDataService reportDataService;

    /**
     * Test for method <b>findById()</b> in ReportDataService
     * <br>
     * Mockito: when <b>ReportDataRepository::findById</b> method called, returns <b>expected</b> object
     */
    @Test
    public void findByIdTest() {
        ReportData expected = new ReportData();
        expected.setChatId(1L);
        expected.setDescription("testDescription");
        expected.setReportDays(1L);


        Mockito.when(reportDataRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(expected));

        ReportData actual = reportDataService.findById(1L);

        Assertions.assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        Assertions.assertThat(actual.getReportDays()).isEqualTo(expected.getReportDays());

    }

    /**
     * Test for throwing an exception in method <b>findById()</b> in ReportDataService
     * <br>
     * Mockito: when <b>ReportDataRepository::findById()</b> method called, throws <b>ReportDataNotFoundException</b>
     */
    @Test
    public void findByIdExceptionTest() {
        Mockito.when(reportDataRepositoryMock.findById(any(Long.class))).thenThrow(ReportDataNotFoundException.class);

        org.junit.jupiter.api.Assertions.assertThrows(ReportDataNotFoundException.class, () -> reportDataService.findById(1L));
    }

    /**
     * Test for method <b>findByChatId()</b> in ReportDataService
     * <br>
     * Mockito: when <b>ReportDataRepository::findByChatId()</b> method called, returns <b>expected</b> object
     */
    @Test
    public void findByChatIdTest() {
        ReportData expected = new ReportData();
        expected.setChatId(1L);
        expected.setDescription("testDescription");
        expected.setReportDays(1L);

        Mockito.when(reportDataRepositoryMock.findByChatId(any(Long.class))).thenReturn(Optional.of(expected));

        Optional<ReportData> actual = reportDataService.findByChatId(1L);

        Assertions.assertThat(actual.get().getChatId()).isEqualTo(expected.getChatId());
        Assertions.assertThat(actual.get().getDescription()).isEqualTo(expected.getDescription());
        Assertions.assertThat(actual.get().getReportDays()).isEqualTo(expected.getReportDays());
    }

    /**
     * Test for method <b>findListByChatId()</b> in ReportDataService
     * <br>
     * Mockito: when <b>ReportDataRepository::findListByChatId()</b> method called, returns <b>expected</b> collection
     */
    @Test
    public void findListByChatIdTest() {
        Collection<ReportData> expected = new ArrayList<>();

        ReportData testReport1 = new ReportData();
        testReport1.setChatId(1L);
        testReport1.setDescription("testDescription1");
        testReport1.setReportDays(1L);
        expected.add(testReport1);

        ReportData testReport2 = new ReportData();
        testReport2.setChatId(1L);
        testReport2.setDescription("testDescription2");
        testReport2.setReportDays(2L);
        expected.add(testReport2);

        ReportData testReport3 = new ReportData();
        testReport3.setChatId(1L);
        testReport3.setDescription("testDescription3");
        testReport3.setReportDays(3L);
        expected.add(testReport3);

        Mockito.when(reportDataRepositoryMock.findListByChatId(any(Long.class))).thenReturn(expected);

        Collection<ReportData> actual = reportDataService.findListByChatId(1L);

        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    /**
     * Test for method <b>getAll()</b> in ReportDataService
     * <br>
     * Mockito: when <b>ReportDataRepository::findAll()</b> method called, returns <b>expected</b> collection
     */
    @Test
    public void findAllTest() {
        List<ReportData> expected = new ArrayList<>();

        ReportData testReport1 = new ReportData();
        testReport1.setChatId(1L);
        testReport1.setDescription("testDescription1");
        testReport1.setReportDays(1L);
        expected.add(testReport1);

        ReportData testReport2 = new ReportData();
        testReport2.setChatId(1L);
        testReport2.setDescription("testDescription2");
        testReport2.setReportDays(2L);
        expected.add(testReport2);

        ReportData testReport3 = new ReportData();
        testReport3.setChatId(1L);
        testReport3.setDescription("testDescription3");
        testReport3.setReportDays(3L);
        expected.add(testReport3);

        Mockito.when(reportDataRepositoryMock.findAll()).thenReturn(expected);

        Collection<ReportData> actual = reportDataService.getAll();

        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}