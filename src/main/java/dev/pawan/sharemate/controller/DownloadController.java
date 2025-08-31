package dev.pawan.sharemate.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pawan.sharemate.repository.UserRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DownloadController {
    private List<Future<?>> futures = new ArrayList<>();
    private ExecutorService exe = Executors.newFixedThreadPool(5);
    private final UserRepository userRepository;

    @GetMapping("/keepalive")
    public void download(HttpServletResponse response) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=student" + currentDateTime + ".xlsx";
        userRepository.existsByUsername("lol");
        response.setHeader(headerKey, headerValue);
        for (int i = 0; i < 5; i++) {
            Map<Integer, Integer> m = IntStream.range(0, 100).boxed()
                    .collect(Collectors.toMap(ii -> ii, ii -> ii * 100));
            XSSFSheet sheet = workbook.createSheet();
            Runnable r = () -> WriteIntoSheet(sheet, m);
            // WriteIntoSheet(sheet, m);
            futures.add(exe.submit(r));
        }
        for (Future<?> future : futures) {
            try {
                // get() will block until the task is complete
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Main thread was interrupted while waiting for tasks.");
            } catch (ExecutionException e) {
                System.err.println("A task encountered an exception: " + e.getCause());
            }
        }
        exe.shutdown();
        ServletOutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void WriteIntoSheet(XSSFSheet sheet, Map<Integer, Integer> data) {
        sheet.autoSizeColumn(2);
        int rowCount = 0;
        for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue((Integer) entry.getKey());
            row.createCell(1).setCellValue((Integer) entry.getValue());
        }
    }
}
