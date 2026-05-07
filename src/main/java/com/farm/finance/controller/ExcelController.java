package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.*;
import com.farm.finance.repository.ChickenBatchRepository;
import com.farm.finance.repository.CustomerRepository;
import com.farm.finance.service.*;
import com.farm.finance.util.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final CustomerService customerService;
    private final SupplierService supplierService;
    private final ProductService productService;
    private final ChickenBatchService chickenBatchService;
    private final ChickenSaleService chickenSaleService;
    private final EggSaleService eggSaleService;
    private final FinanceRecordService financeRecordService;

    // Repository for lookups
    private final ChickenBatchRepository chickenBatchRepository;
    private final CustomerRepository customerRepository;

    // ==================== 导出功能 ====================

    /**
     * 导出客户数据
     */
    @GetMapping("/export/customers")
    public void exportCustomers(HttpServletResponse response) throws IOException {
        List<Customer> customers = customerService.findByIsActiveTrue();
        
        try (Workbook workbook = ExcelUtils.createWorkbook()) {
            Sheet sheet = workbook.createSheet("客户数据");
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "客户编号", "客户名称", "联系电话", "地址", "创建时间"};
            CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                ExcelUtils.setCellValue(headerRow, i, headers[i], headerStyle);
            }
            
            // 填充数据
            CellStyle cellStyle = ExcelUtils.createCellStyle(workbook);
            int rowNum = 1;
            for (Customer customer : customers) {
                Row row = sheet.createRow(rowNum++);
                ExcelUtils.setCellValue(row, 0, customer.getId(), cellStyle);
                ExcelUtils.setCellValue(row, 1, customer.getCustomerNo(), cellStyle);
                ExcelUtils.setCellValue(row, 2, customer.getName(), cellStyle);
                ExcelUtils.setCellValue(row, 3, customer.getPhone(), cellStyle);
                ExcelUtils.setCellValue(row, 4, customer.getAddress(), cellStyle);
                ExcelUtils.setCellValue(row, 5, formatDateTime(customer.getCreatedTime()), cellStyle);
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ExcelUtils.setResponseHeader(response, "客户数据");
            workbook.write(response.getOutputStream());
        }
    }

    /**
     * 导出供应商数据
     */
    @GetMapping("/export/suppliers")
    public void exportSuppliers(HttpServletResponse response) throws IOException {
        List<Supplier> suppliers = supplierService.findByIsActiveTrue();
        
        try (Workbook workbook = ExcelUtils.createWorkbook()) {
            Sheet sheet = workbook.createSheet("供应商数据");
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "供应商编号", "供应商名称", "联系人", "联系电话", "创建时间"};
            CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                ExcelUtils.setCellValue(headerRow, i, headers[i], headerStyle);
            }
            
            CellStyle cellStyle = ExcelUtils.createCellStyle(workbook);
            int rowNum = 1;
            for (Supplier supplier : suppliers) {
                Row row = sheet.createRow(rowNum++);
                ExcelUtils.setCellValue(row, 0, supplier.getId(), cellStyle);
                ExcelUtils.setCellValue(row, 1, supplier.getSupplierNo(), cellStyle);
                ExcelUtils.setCellValue(row, 2, supplier.getName(), cellStyle);
                ExcelUtils.setCellValue(row, 3, supplier.getContactPerson(), cellStyle);
                ExcelUtils.setCellValue(row, 4, supplier.getPhone(), cellStyle);
                ExcelUtils.setCellValue(row, 5, formatDateTime(supplier.getCreatedTime()), cellStyle);
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ExcelUtils.setResponseHeader(response, "供应商数据");
            workbook.write(response.getOutputStream());
        }
    }

    /**
     * 导出产品数据
     */
    @GetMapping("/export/products")
    public void exportProducts(HttpServletResponse response) throws IOException {
        List<Product> products = productService.findByIsActiveTrue();
        
        try (Workbook workbook = ExcelUtils.createWorkbook()) {
            Sheet sheet = workbook.createSheet("产品数据");
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "产品编号", "产品名称", "产品类型", "单位", "采购价", "销售价", "批发价", "创建时间"};
            CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                ExcelUtils.setCellValue(headerRow, i, headers[i], headerStyle);
            }
            
            CellStyle cellStyle = ExcelUtils.createCellStyle(workbook);
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                ExcelUtils.setCellValue(row, 0, product.getId(), cellStyle);
                ExcelUtils.setCellValue(row, 1, product.getProductNo(), cellStyle);
                ExcelUtils.setCellValue(row, 2, product.getName(), cellStyle);
                ExcelUtils.setCellValue(row, 3, product.getProductType(), cellStyle);
                ExcelUtils.setCellValue(row, 4, product.getUnit(), cellStyle);
                ExcelUtils.setCellValue(row, 5, product.getPurchasePrice(), cellStyle);
                ExcelUtils.setCellValue(row, 6, product.getSalePrice(), cellStyle);
                ExcelUtils.setCellValue(row, 7, product.getWholesalePrice(), cellStyle);
                ExcelUtils.setCellValue(row, 8, formatDateTime(product.getCreatedTime()), cellStyle);
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ExcelUtils.setResponseHeader(response, "产品数据");
            workbook.write(response.getOutputStream());
        }
    }

    /**
     * 导出肉鸡批次数据
     */
    @GetMapping("/export/chicken-batches")
    public void exportChickenBatches(HttpServletResponse response) throws IOException {
        List<ChickenBatch> batches = chickenBatchService.findByIsActiveTrue();
        
        try (Workbook workbook = ExcelUtils.createWorkbook()) {
            Sheet sheet = workbook.createSheet("肉鸡批次数据");
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "批次编号", "鸡群名称", "品种", "鸡舍ID", "进雏日期", "进雏数量", "当前存栏", "当前日龄", "状态", "创建时间"};
            CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                ExcelUtils.setCellValue(headerRow, i, headers[i], headerStyle);
            }
            
            CellStyle cellStyle = ExcelUtils.createCellStyle(workbook);
            int rowNum = 1;
            for (ChickenBatch batch : batches) {
                Row row = sheet.createRow(rowNum++);
                ExcelUtils.setCellValue(row, 0, batch.getId(), cellStyle);
                ExcelUtils.setCellValue(row, 1, batch.getBatchNo(), cellStyle);
                ExcelUtils.setCellValue(row, 2, batch.getGroupName(), cellStyle);
                ExcelUtils.setCellValue(row, 3, batch.getBreed(), cellStyle);
                ExcelUtils.setCellValue(row, 4, batch.getHouseId(), cellStyle);
                ExcelUtils.setCellValue(row, 5, batch.getEntryDate(), cellStyle);
                ExcelUtils.setCellValue(row, 6, batch.getEntryQuantity(), cellStyle);
                ExcelUtils.setCellValue(row, 7, batch.getCurrentQuantity(), cellStyle);
                ExcelUtils.setCellValue(row, 8, batch.getCurrentAge(), cellStyle);
                ExcelUtils.setCellValue(row, 9, batch.getStatus(), cellStyle);
                ExcelUtils.setCellValue(row, 10, formatDateTime(batch.getCreatedTime()), cellStyle);
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ExcelUtils.setResponseHeader(response, "肉鸡批次数据");
            workbook.write(response.getOutputStream());
        }
    }

    /**
     * 导出肉鸡销售数据
     */
    @GetMapping("/export/chicken-sales")
    public void exportChickenSales(HttpServletResponse response) throws IOException {
        List<ChickenSale> sales = chickenSaleService.findByIsActiveTrue();
        
        try (Workbook workbook = ExcelUtils.createWorkbook()) {
            Sheet sheet = workbook.createSheet("肉鸡销售数据");
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "销售单号", "批次ID", "销售日期", "数量", "重量(斤)", "单价(元/斤)", "总金额", "客户ID", "创建时间"};
            CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                ExcelUtils.setCellValue(headerRow, i, headers[i], headerStyle);
            }
            
            CellStyle cellStyle = ExcelUtils.createCellStyle(workbook);
            int rowNum = 1;
            for (ChickenSale sale : sales) {
                Row row = sheet.createRow(rowNum++);
                ExcelUtils.setCellValue(row, 0, sale.getId(), cellStyle);
                ExcelUtils.setCellValue(row, 1, sale.getSaleNo(), cellStyle);
                ExcelUtils.setCellValue(row, 2, sale.getBatchId(), cellStyle);
                ExcelUtils.setCellValue(row, 3, sale.getSaleDate(), cellStyle);
                ExcelUtils.setCellValue(row, 4, sale.getQuantity(), cellStyle);
                ExcelUtils.setCellValue(row, 5, sale.getWeight(), cellStyle);
                ExcelUtils.setCellValue(row, 6, sale.getUnitPrice(), cellStyle);
                ExcelUtils.setCellValue(row, 7, sale.getTotalAmount(), cellStyle);
                ExcelUtils.setCellValue(row, 8, sale.getCustomerId(), cellStyle);
                ExcelUtils.setCellValue(row, 9, formatDateTime(sale.getCreatedTime()), cellStyle);
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ExcelUtils.setResponseHeader(response, "肉鸡销售数据");
            workbook.write(response.getOutputStream());
        }
    }

    /**
     * 导出鸡蛋销售数据
     */
    @GetMapping("/export/egg-sales")
    public void exportEggSales(HttpServletResponse response) throws IOException {
        List<EggSale> sales = eggSaleService.findByIsActiveTrue();
        
        try (Workbook workbook = ExcelUtils.createWorkbook()) {
            Sheet sheet = workbook.createSheet("鸡蛋销售数据");
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "销售单号", "销售日期", "产品ID", "销售数量", "重量(斤)", "单价(元/斤)", "总金额", "客户ID", "创建时间"};
            CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                ExcelUtils.setCellValue(headerRow, i, headers[i], headerStyle);
            }
            
            CellStyle cellStyle = ExcelUtils.createCellStyle(workbook);
            int rowNum = 1;
            for (EggSale sale : sales) {
                Row row = sheet.createRow(rowNum++);
                ExcelUtils.setCellValue(row, 0, sale.getId(), cellStyle);
                ExcelUtils.setCellValue(row, 1, sale.getSaleNo(), cellStyle);
                ExcelUtils.setCellValue(row, 2, sale.getSaleDate(), cellStyle);
                ExcelUtils.setCellValue(row, 3, sale.getProductId(), cellStyle);
                ExcelUtils.setCellValue(row, 4, sale.getSaleQuantity(), cellStyle);
                ExcelUtils.setCellValue(row, 5, sale.getSaleWeight(), cellStyle);
                ExcelUtils.setCellValue(row, 6, sale.getUnitPrice(), cellStyle);
                ExcelUtils.setCellValue(row, 7, sale.getTotalAmount(), cellStyle);
                ExcelUtils.setCellValue(row, 8, sale.getCustomerId(), cellStyle);
                ExcelUtils.setCellValue(row, 9, formatDateTime(sale.getCreatedTime()), cellStyle);
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ExcelUtils.setResponseHeader(response, "鸡蛋销售数据");
            workbook.write(response.getOutputStream());
        }
    }

    /**
     * 导出财务记录
     */
    @GetMapping("/export/finance-records")
    public void exportFinanceRecords(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletResponse response) throws IOException {
        
        List<FinanceRecord> records = financeRecordService.findByRecordDateBetween(startDate, endDate);
        
        try (Workbook workbook = ExcelUtils.createWorkbook()) {
            Sheet sheet = workbook.createSheet("财务记录");
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "记录编号", "记录日期", "资金类型", "金额", "支出类型", "收入类型", "客户ID", "供应商ID", "支付方式", "描述", "创建时间"};
            CellStyle headerStyle = ExcelUtils.createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                ExcelUtils.setCellValue(headerRow, i, headers[i], headerStyle);
            }
            
            CellStyle cellStyle = ExcelUtils.createCellStyle(workbook);
            int rowNum = 1;
            for (FinanceRecord record : records) {
                Row row = sheet.createRow(rowNum++);
                ExcelUtils.setCellValue(row, 0, record.getId(), cellStyle);
                ExcelUtils.setCellValue(row, 1, record.getRecordNo(), cellStyle);
                ExcelUtils.setCellValue(row, 2, record.getRecordDate(), cellStyle);
                ExcelUtils.setCellValue(row, 3, record.getMoneyType(), cellStyle);
                ExcelUtils.setCellValue(row, 4, record.getAmount(), cellStyle);
                ExcelUtils.setCellValue(row, 5, record.getCostType(), cellStyle);
                ExcelUtils.setCellValue(row, 6, record.getIncomeType(), cellStyle);
                ExcelUtils.setCellValue(row, 7, record.getCustomerId(), cellStyle);
                ExcelUtils.setCellValue(row, 8, record.getSupplierId(), cellStyle);
                ExcelUtils.setCellValue(row, 9, record.getPaymentMethod(), cellStyle);
                ExcelUtils.setCellValue(row, 10, record.getDescription(), cellStyle);
                ExcelUtils.setCellValue(row, 11, formatDateTime(record.getCreatedTime()), cellStyle);
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ExcelUtils.setResponseHeader(response, "财务记录_" + startDate + "_" + endDate);
            workbook.write(response.getOutputStream());
        }
    }

    // ==================== 导入功能 ====================

    /**
     * 导入客户数据
     */
    @PostMapping("/import/customers")
    public Result<String> importCustomers(@RequestParam("file") MultipartFile file) throws IOException {
        List<Customer> customers = parseCustomerExcel(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        
        for (Customer customer : customers) {
            try {
                customer.setIsActive(true);
                customer.setCreatedTime(LocalDateTime.now());
                customerService.save(customer);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        return Result.success("导入完成：成功 " + successCount + " 条，失败 " + failCount + " 条");
    }

    /**
     * 导入供应商数据
     */
    @PostMapping("/import/suppliers")
    public Result<String> importSuppliers(@RequestParam("file") MultipartFile file) throws IOException {
        List<Supplier> suppliers = parseSupplierExcel(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        
        for (Supplier supplier : suppliers) {
            try {
                supplier.setIsActive(true);
                supplier.setCreatedTime(LocalDateTime.now());
                supplierService.save(supplier);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        return Result.success("导入完成：成功 " + successCount + " 条，失败 " + failCount + " 条");
    }

    /**
     * 导入产品数据
     */
    @PostMapping("/import/products")
    public Result<String> importProducts(@RequestParam("file") MultipartFile file) throws IOException {
        List<Product> products = parseProductExcel(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        
        for (Product product : products) {
            try {
                product.setIsActive(true);
                product.setCreatedTime(LocalDateTime.now());
                productService.save(product);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        return Result.success("导入完成：成功 " + successCount + " 条，失败 " + failCount + " 条");
    }

    /**
     * 导入肉鸡销售数据
     */
    @PostMapping("/import/chicken-sales")
    public Result<String> importChickenSales(@RequestParam("file") MultipartFile file) throws IOException {
        List<Object> result = parseChickenSaleExcelWithErrors(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        String headerInfo = "";

        for (Object obj : result) {
            if (obj instanceof ChickenSale) {
                ChickenSale sale = (ChickenSale) obj;
                try {
                    sale.setIsActive(true);
                    sale.setCreatedTime(LocalDateTime.now());
                    chickenSaleService.save(sale);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errors.add("保存失败：" + e.getMessage());
                }
            } else if (obj instanceof String) {
                String msg = (String) obj;
                if (msg.startsWith("识别到的表头：")) {
                    headerInfo = msg;
                } else {
                    failCount++;
                    errors.add(msg);
                }
            }
        }

        String message = headerInfo + "\n导入完成：成功 " + successCount + " 条，失败 " + failCount + " 条";
        if (!errors.isEmpty() && errors.size() <= 5) {
            message += "。错误：" + String.join("；", errors);
        } else if (!errors.isEmpty()) {
            message += "。前5个错误：" + String.join("；", errors.subList(0, 5));
        }

        return Result.success(message);
    }

    // ==================== 解析方法 ====================

    private List<Customer> parseCustomerExcel(InputStream inputStream) throws IOException {
        List<Customer> customers = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        // 跳过表头
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty() || (row.get(0).isEmpty() && row.get(2).isEmpty())) {
                continue;
            }
            
            Customer customer = new Customer();
            if (!row.get(1).isEmpty()) customer.setCustomerNo(row.get(1));
            if (!row.get(2).isEmpty()) customer.setName(row.get(2));
            if (row.size() > 3 && !row.get(3).isEmpty()) customer.setPhone(row.get(3));
            if (row.size() > 4 && !row.get(4).isEmpty()) customer.setAddress(row.get(4));
            
            if (customer.getName() != null && !customer.getName().isEmpty()) {
                customers.add(customer);
            }
        }
        
        return customers;
    }

    private List<Supplier> parseSupplierExcel(InputStream inputStream) throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty() || (row.get(0).isEmpty() && row.get(2).isEmpty())) {
                continue;
            }
            
            Supplier supplier = new Supplier();
            if (!row.get(1).isEmpty()) supplier.setSupplierNo(row.get(1));
            if (!row.get(2).isEmpty()) supplier.setName(row.get(2));
            if (row.size() > 3 && !row.get(3).isEmpty()) supplier.setContactPerson(row.get(3));
            if (row.size() > 4 && !row.get(4).isEmpty()) supplier.setPhone(row.get(4));
            
            if (supplier.getName() != null && !supplier.getName().isEmpty()) {
                suppliers.add(supplier);
            }
        }
        
        return suppliers;
    }

    private List<Product> parseProductExcel(InputStream inputStream) throws IOException {
        List<Product> products = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty() || (row.get(0).isEmpty() && row.get(2).isEmpty())) {
                continue;
            }
            
            Product product = new Product();
            if (!row.get(1).isEmpty()) product.setProductNo(row.get(1));
            if (!row.get(2).isEmpty()) product.setName(row.get(2));
            if (row.size() > 3 && !row.get(3).isEmpty()) product.setProductType(row.get(3));
            if (row.size() > 4 && !row.get(4).isEmpty()) product.setUnit(row.get(4));
            if (row.size() > 5 && !row.get(5).isEmpty()) product.setPurchasePrice(new BigDecimal(row.get(5)));
            if (row.size() > 6 && !row.get(6).isEmpty()) product.setSalePrice(new BigDecimal(row.get(6)));
            if (row.size() > 7 && !row.get(7).isEmpty()) product.setWholesalePrice(new BigDecimal(row.get(7)));
            
            if (product.getName() != null && !product.getName().isEmpty()) {
                products.add(product);
            }
        }
        
        return products;
    }

    // ==================== 工具方法 ====================

    private List<Object> parseChickenSaleExcelWithErrors(InputStream inputStream) throws IOException {
        List<Object> result = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);

        if (data.size() < 2) {
            result.add("Excel文件为空或只有表头");
            return result;
        }

        // 解析表头，建立列名到索引的映射
        List<String> headers = data.get(0);
        java.util.Map<String, Integer> colIndex = new java.util.HashMap<>();
        StringBuilder headerInfo = new StringBuilder("识别到的表头：");

        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i).trim();
            headerInfo.append("[").append(i).append(":").append(header).append("] ");
            String headerLower = header.toLowerCase();

            switch (headerLower) {
                case "鸡舍":
                case "舍":
                case "house":
                case "批次":
                case "批次编号":
                case "批次号":
                case "batch":
                case "batchno":
                    colIndex.put("house", i);
                    break;
                case "销售日期":
                case "日期":
                case "date":
                    colIndex.put("date", i);
                    break;
                case "数量":
                case "quantity":
                    colIndex.put("quantity", i);
                    break;
                case "重量":
                case "weight":
                    colIndex.put("weight", i);
                    break;
                case "单价":
                case "price":
                    colIndex.put("price", i);
                    break;
                case "总金额":
                case "金额":
                case "总额":
                case "amount":
                    colIndex.put("amount", i);
                    break;
                case "客户":
                case "customer":
                    colIndex.put("customer", i);
                    break;
                case "单号":
                case "编号":
                case "销售单号":
                case "saleno":
                    colIndex.put("saleno", i);
                    break;
            }
        }

        result.add(headerInfo.toString() + " | 列映射：批次=" + colIndex.get("house") +
            ", 日期=" + colIndex.get("date") + ", 数量=" + colIndex.get("quantity") +
            ", 重量=" + colIndex.get("weight") + ", 单价=" + colIndex.get("price") +
            ", 金额=" + colIndex.get("amount"));

        if (data.size() > 1) {
            List<String> firstRow = data.get(1);
            StringBuilder dataInfo = new StringBuilder("第2行数据：");
            for (int j = 0; j < firstRow.size() && j < 8; j++) {
                dataInfo.append("[").append(j).append(":").append(firstRow.get(j)).append("] ");
            }
            result.add(dataInfo.toString());
        }

        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty()) continue;

            int rowNum = i + 1;

            Integer houseIdx = colIndex.get("house");
            if (houseIdx == null || houseIdx >= row.size() || row.get(houseIdx).isEmpty()) {
                result.add("第" + rowNum + "行：缺少批次信息");
                continue;
            }

            String batchRef = row.get(houseIdx).trim();
            Optional<ChickenBatch> batch = chickenBatchRepository.findByBatchNo(batchRef);
            if (batch.isEmpty()) {
                batch = chickenBatchRepository.findByBatchName(batchRef);
            }
            if (batch.isEmpty()) {
                result.add("第" + rowNum + "行：批次\"" + batchRef + "\"不存在，请先在系统中添加该批次");
                continue;
            }

            Integer dateIdx = colIndex.get("date");
            if (dateIdx == null || dateIdx >= row.size() || row.get(dateIdx).isEmpty()) {
                result.add("第" + rowNum + "行：缺少销售日期");
                continue;
            }

            LocalDate saleDate = parseDate(row.get(dateIdx));
            if (saleDate == null) {
                result.add("第" + rowNum + "行：日期格式错误\"" + row.get(dateIdx) + "\"");
                continue;
            }

            ChickenSale sale = new ChickenSale();
            sale.setBatchId(batch.get().getId());
            sale.setSaleDate(saleDate);

            Integer saleNoIdx = colIndex.get("saleno");
            if (saleNoIdx != null && saleNoIdx < row.size() && !row.get(saleNoIdx).isEmpty()) {
                String inputSaleNo = row.get(saleNoIdx).trim();
                if (chickenSaleService.existsBySaleNo(inputSaleNo)) {
                    result.add("第" + rowNum + "行：销售单号\"" + inputSaleNo + "\"已存在，请修改或留空让系统自动生成");
                    continue;
                }
                sale.setSaleNo(inputSaleNo);
            } else {
                String timestamp = String.valueOf(System.currentTimeMillis()).substring(9);
                sale.setSaleNo("CS" + saleDate.toString().replace("-", "") + timestamp);
            }

            Integer qtyIdx = colIndex.get("quantity");
            int quantity = 0;
            if (qtyIdx != null && qtyIdx < row.size() && !row.get(qtyIdx).isEmpty()) {
                try { quantity = Integer.parseInt(row.get(qtyIdx).trim()); } catch (Exception ignored) {}
            }
            sale.setQuantity(quantity);

            Integer weightIdx = colIndex.get("weight");
            BigDecimal weight = BigDecimal.ZERO;
            if (weightIdx != null && weightIdx < row.size() && !row.get(weightIdx).isEmpty()) {
                try { weight = new BigDecimal(row.get(weightIdx).trim()); } catch (Exception ignored) {}
            }
            sale.setWeight(weight);

            Integer priceIdx = colIndex.get("price");
            BigDecimal unitPrice = BigDecimal.ZERO;
            if (priceIdx != null && priceIdx < row.size() && !row.get(priceIdx).isEmpty()) {
                try { unitPrice = new BigDecimal(row.get(priceIdx).trim()); } catch (Exception ignored) {}
            }
            sale.setUnitPrice(unitPrice);

            Integer amountIdx = colIndex.get("amount");
            BigDecimal totalAmount = BigDecimal.ZERO;
            if (amountIdx != null && amountIdx < row.size() && !row.get(amountIdx).isEmpty()) {
                try { totalAmount = new BigDecimal(row.get(amountIdx).trim()); } catch (Exception ignored) {}
            }
            sale.setTotalAmount(totalAmount);

            Integer customerIdx = colIndex.get("customer");
            if (customerIdx != null && customerIdx < row.size() && !row.get(customerIdx).isEmpty()) {
                String customerRef = row.get(customerIdx).trim();
                Optional<Customer> customer = customerRepository.findByName(customerRef);
                if (customer.isEmpty()) {
                    customer = customerRepository.findByCustomerNo(customerRef);
                }
                customer.ifPresent(c -> sale.setCustomerId(c.getId()));
            }

            result.add(sale);
        }

        return result;
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy年MM月dd日"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
            };
            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDate.parse(dateStr.trim(), formatter);
                } catch (Exception ignored) {}
            }
            return LocalDate.parse(dateStr.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
