package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.*;
import com.farm.finance.repository.ChickenBatchRepository;
import com.farm.finance.repository.ChickenHouseRepository;
import com.farm.finance.repository.ProductRepository;
import com.farm.finance.repository.CustomerRepository;
import com.farm.finance.repository.SupplierRepository;
import com.farm.finance.service.*;
import com.farm.finance.util.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
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
    private final ChickenSaleService chickenSaleService;
    private final EggSaleService eggSaleService;
    private final FinanceRecordService financeRecordService;
    private final ChickenHouseService chickenHouseService;
    private final DeathRecordService deathRecordService;
    private final InventoryService inventoryService;
    private final PurchaseOrderService purchaseOrderService;
    private final SalesOrderService salesOrderService;
    
    // Repository for lookups
    private final ChickenHouseRepository chickenHouseRepository;
    private final ChickenBatchRepository chickenBatchRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;

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
            String[] headers = {"ID", "产品编号", "产品名称", "产品类型", "单位", "销售价", "批发价", "创建时间"};
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
                ExcelUtils.setCellValue(row, 5, product.getSalePrice(), cellStyle);
                ExcelUtils.setCellValue(row, 6, product.getWholesalePrice(), cellStyle);
                ExcelUtils.setCellValue(row, 7, formatDateTime(product.getCreatedTime()), cellStyle);
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ExcelUtils.setResponseHeader(response, "产品数据");
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
     * 导入鸡舍数据
     */
    @PostMapping("/import/chicken-houses")
    public Result<String> importChickenHouses(@RequestParam("file") MultipartFile file) throws IOException {
        List<ChickenHouse> houses = parseChickenHouseExcel(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        
        for (ChickenHouse house : houses) {
            try {
                house.setIsActive(true);
                house.setCreatedTime(LocalDateTime.now());
                chickenHouseService.save(house);
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
            // 彻底清理不可见字符
            String header = headers.get(i).trim().replaceAll("[\\s\\u00A0\\u3000\\u200B-\\u200F\\u2028-\\u202F\\uFEFF]+", "");
            headerInfo.append("[").append(i).append(":").append(header).append("(len=").append(header.length()).append(")]");
            String headerLower = header.toLowerCase();
            
            // 精确匹配
            switch (headerLower) {
                case "批次":
                case "批次id":
                case "批次编号":
                case "batch":
                case "batchid":
                    colIndex.put("batch", i);
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
        
        // 添加表头识别信息
        result.add(headerInfo.toString() + " | 列映射：批次=" + colIndex.get("batch") +
            ", 日期=" + colIndex.get("date") + ", 数量=" + colIndex.get("quantity") + 
            ", 重量=" + colIndex.get("weight") + ", 单价=" + colIndex.get("price") + 
            ", 金额=" + colIndex.get("amount"));
        
        // 添加第一行数据的调试信息
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
            
            // 获取批次（支持批次编号或批次名称）
            Integer batchIdx = colIndex.get("batch");
            if (batchIdx == null || batchIdx >= row.size() || row.get(batchIdx).isEmpty()) {
                result.add("第" + rowNum + "行：缺少批次信息");
                continue;
            }

            String batchRef = normalizeNumericString(row.get(batchIdx).trim());
            String originalRef = row.get(batchIdx).trim(); // 保留原始值用于模糊匹配
            Optional<ChickenBatch> batch = chickenBatchRepository.findByBatchNo(batchRef);
            if (batch.isEmpty()) {
                batch = chickenBatchRepository.findByBatchName(batchRef);
            }
            if (batch.isEmpty() && !batchRef.equals(originalRef)) {
                // 用原始值再试一次（normalizeNumericString可能改变了值）
                batch = chickenBatchRepository.findByBatchNo(originalRef);
                if (batch.isEmpty()) {
                    batch = chickenBatchRepository.findByBatchName(originalRef);
                }
            }
            if (batch.isEmpty()) {
                // 尝试按ID查找（处理导出文件中直接使用ID的情况）
                try {
                    Long batchId = Long.parseLong(batchRef);
                    batch = chickenBatchRepository.findById(batchId);
                } catch (NumberFormatException ignored) {}
            }
            if (batch.isEmpty()) {
                // 最后尝试关键字模糊匹配
                List<ChickenBatch> matches = chickenBatchRepository.searchByKeyword(batchRef);
                if (matches.size() == 1) {
                    batch = Optional.of(matches.get(0));
                } else if (matches.size() > 1) {
                    result.add("第" + rowNum + "行：批次\"" + batchRef + "\"匹配到多个结果，请使用更精确的批次编号");
                    continue;
                }
            }
            if (batch.isEmpty()) {
                result.add("第" + rowNum + "行：批次\"" + batchRef + "\"不存在，请先在系统中添加该批次");
                continue;
            }
            
            // 获取销售日期
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
            
            // 销售单号 - 必填，自动生成唯一单号
            Integer saleNoIdx = colIndex.get("saleno");
            if (saleNoIdx != null && saleNoIdx < row.size() && !row.get(saleNoIdx).isEmpty()) {
                String inputSaleNo = row.get(saleNoIdx).trim();
                // 检查单号是否已存在
                if (chickenSaleService.existsBySaleNo(inputSaleNo)) {
                    result.add("第" + rowNum + "行：销售单号\"" + inputSaleNo + "\"已存在，请修改或留空让系统自动生成");
                    continue;
                }
                sale.setSaleNo(inputSaleNo);
            } else {
                // 自动生成唯一销售单号：CS + 日期 + 时间戳后4位
                String timestamp = String.valueOf(System.currentTimeMillis()).substring(9);
                sale.setSaleNo("CS" + saleDate.toString().replace("-", "") + timestamp);
            }
            
            // 数量 - 必填
            Integer qtyIdx = colIndex.get("quantity");
            int quantity = 0;
            if (qtyIdx != null && qtyIdx < row.size() && !row.get(qtyIdx).isEmpty()) {
                try { quantity = Integer.parseInt(row.get(qtyIdx).trim()); } catch (Exception ignored) {}
            }
            sale.setQuantity(quantity);
            
            // 重量 - 必填，默认0
            Integer weightIdx = colIndex.get("weight");
            BigDecimal weight = BigDecimal.ZERO;
            if (weightIdx != null && weightIdx < row.size() && !row.get(weightIdx).isEmpty()) {
                try { weight = new BigDecimal(row.get(weightIdx).trim()); } catch (Exception ignored) {}
            }
            sale.setWeight(weight);
            
            // 单价 - 必填，默认0
            Integer priceIdx = colIndex.get("price");
            BigDecimal unitPrice = BigDecimal.ZERO;
            if (priceIdx != null && priceIdx < row.size() && !row.get(priceIdx).isEmpty()) {
                try { unitPrice = new BigDecimal(row.get(priceIdx).trim()); } catch (Exception ignored) {}
            }
            sale.setUnitPrice(unitPrice);
            
            // 总金额 - 必填，默认0
            Integer amountIdx = colIndex.get("amount");
            BigDecimal totalAmount = BigDecimal.ZERO;
            if (amountIdx != null && amountIdx < row.size() && !row.get(amountIdx).isEmpty()) {
                try { totalAmount = new BigDecimal(row.get(amountIdx).trim()); } catch (Exception ignored) {}
            }
            sale.setTotalAmount(totalAmount);
            
            // 客户
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

    /**
     * 导入鸡蛋销售数据
     */
    @PostMapping("/import/egg-sales")
    public Result<String> importEggSales(@RequestParam("file") MultipartFile file) throws IOException {
        List<EggSale> sales = parseEggSaleExcel(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        
        for (EggSale sale : sales) {
            try {
                sale.setIsActive(true);
                sale.setCreatedTime(LocalDateTime.now());
                eggSaleService.save(sale);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        return Result.success("导入完成：成功 " + successCount + " 条，失败 " + failCount + " 条");
    }

    /**
     * 导入死亡记录数据
     */
    @PostMapping("/import/death-records")
    public Result<String> importDeathRecords(@RequestParam("file") MultipartFile file) throws IOException {
        List<DeathRecord> records = parseDeathRecordExcel(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        
        for (DeathRecord record : records) {
            try {
                record.setIsActive(true);
                record.setCreatedTime(LocalDateTime.now());
                deathRecordService.save(record);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        return Result.success("导入完成：成功 " + successCount + " 条，失败 " + failCount + " 条");
    }

    /**
     * 导入库存数据
     */
    @PostMapping("/import/inventory")
    public Result<String> importInventory(@RequestParam("file") MultipartFile file) throws IOException {
        List<Inventory> inventories = parseInventoryExcel(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        
        for (Inventory inventory : inventories) {
            try {
                inventory.setIsActive(true);
                inventory.setStatus(true);
                inventory.setCreatedTime(LocalDateTime.now());
                inventory.setUpdatedTime(LocalDateTime.now());
                inventoryService.save(inventory);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        return Result.success("导入完成：成功 " + successCount + " 条，失败 " + failCount + " 条");
    }

    /**
     * 导入财务记录数据
     */
    @PostMapping("/import/finance-records")
    public Result<String> importFinanceRecords(@RequestParam("file") MultipartFile file) throws IOException {
        List<FinanceRecord> records = parseFinanceRecordExcel(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        
        for (FinanceRecord record : records) {
            try {
                record.setIsActive(true);
                record.setCreatedTime(LocalDateTime.now());
                financeRecordService.save(record);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        return Result.success("导入完成：成功 " + successCount + " 条，失败 " + failCount + " 条");
    }

    /**
     * 导入采购订单数据
     */
    @PostMapping("/import/purchase-orders")
    public Result<String> importPurchaseOrders(@RequestParam("file") MultipartFile file) throws IOException {
        List<PurchaseOrder> orders = parsePurchaseOrderExcel(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        
        for (PurchaseOrder order : orders) {
            try {
                order.setIsActive(true);
                order.setCreatedTime(LocalDateTime.now());
                purchaseOrderService.save(order);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        return Result.success("导入完成：成功 " + successCount + " 条，失败 " + failCount + " 条");
    }

    /**
     * 导入销售订单数据
     */
    @PostMapping("/import/sales-orders")
    public Result<String> importSalesOrders(@RequestParam("file") MultipartFile file) throws IOException {
        List<SalesOrder> orders = parseSalesOrderExcel(file.getInputStream());
        int successCount = 0;
        int failCount = 0;
        
        for (SalesOrder order : orders) {
            try {
                order.setIsActive(true);
                order.setCreatedTime(LocalDateTime.now());
                salesOrderService.save(order);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        return Result.success("导入完成：成功 " + successCount + " 条，失败 " + failCount + " 条");
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
            if (row.size() > 5 && !row.get(5).isEmpty()) product.setSalePrice(new BigDecimal(row.get(5)));
            if (row.size() > 6 && !row.get(6).isEmpty()) product.setWholesalePrice(new BigDecimal(row.get(6)));
            
            if (product.getName() != null && !product.getName().isEmpty()) {
                products.add(product);
            }
        }
        
        return products;
    }

    private List<ChickenHouse> parseChickenHouseExcel(InputStream inputStream) throws IOException {
        List<ChickenHouse> houses = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty()) continue;
            
            ChickenHouse house = new ChickenHouse();
            // 必填字段
            if (row.size() > 1 && !row.get(1).isEmpty()) house.setHouseNo(row.get(1));
            if (row.size() > 2 && !row.get(2).isEmpty()) house.setName(row.get(2));
            if (row.size() > 3 && !row.get(3).isEmpty()) house.setCapacity(Integer.parseInt(row.get(3)));
            
            // 可选字段
            if (row.size() > 4 && !row.get(4).isEmpty()) house.setArea(new BigDecimal(row.get(4)));
            if (row.size() > 5 && !row.get(5).isEmpty()) house.setStatus(row.get(5)); // EMPTY/ACTIVE/FINISHED
            
            // 批次相关字段
            if (row.size() > 6 && !row.get(6).isEmpty()) house.setGroupName(row.get(6));
            if (row.size() > 7 && !row.get(7).isEmpty()) house.setBreed(row.get(7));
            if (row.size() > 8 && !row.get(8).isEmpty()) {
                // 供应商：支持名称或编号
                String supplierRef = row.get(8);
                Optional<Supplier> supplier = supplierRepository.findByName(supplierRef);
                if (supplier.isEmpty()) {
                    supplier = supplierRepository.findBySupplierNo(supplierRef);
                }
                supplier.ifPresent(s -> house.setSupplierId(s.getId()));
            }
            if (row.size() > 9 && !row.get(9).isEmpty()) house.setEntryDate(parseDate(row.get(9)));
            if (row.size() > 10 && !row.get(10).isEmpty()) house.setEntryQuantity(Integer.parseInt(row.get(10)));
            if (row.size() > 11 && !row.get(11).isEmpty()) house.setEntryPrice(new BigDecimal(row.get(11)));
            if (row.size() > 12 && !row.get(12).isEmpty()) house.setEntryTotalCost(new BigDecimal(row.get(12)));
            if (row.size() > 13 && !row.get(13).isEmpty()) house.setCurrentQuantity(Integer.parseInt(row.get(13)));
            
            if (house.getHouseNo() != null && house.getName() != null) {
                if (house.getStatus() == null) house.setStatus("EMPTY");
                houses.add(house);
            }
        }
        
        return houses;
    }

    private List<ChickenSale> parseChickenSaleExcel(InputStream inputStream) throws IOException {
        List<ChickenSale> sales = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        if (data.size() < 2) return sales;
        
        // 解析表头，建立列名到索引的映射
        List<String> headers = data.get(0);
        java.util.Map<String, Integer> colIndex = new java.util.HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i).trim().toLowerCase();
            colIndex.put(header, i);
            // 别名映射
            if (header.contains("鸡舍") || header.contains("舍")) colIndex.put("house", i);
            if (header.contains("日期") || header.contains("销售日期")) colIndex.put("date", i);
            if (header.contains("数量")) colIndex.put("quantity", i);
            if (header.contains("重量")) colIndex.put("weight", i);
            if (header.contains("单价")) colIndex.put("price", i);
            if (header.contains("金额") || header.contains("总额")) colIndex.put("amount", i);
            if (header.contains("客户")) colIndex.put("customer", i);
            if (header.contains("单号") || header.contains("编号")) colIndex.put("saleno", i);
        }
        
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty()) continue;
            
            ChickenSale sale = new ChickenSale();
            
            // 销售单号
            Integer saleNoIdx = colIndex.get("saleno");
            if (saleNoIdx != null && saleNoIdx < row.size() && !row.get(saleNoIdx).isEmpty()) {
                sale.setSaleNo(row.get(saleNoIdx));
            }
            
            // 批次：支持批次编号或批次名称
            Integer houseIdx = colIndex.get("house");
            if (houseIdx != null && houseIdx < row.size() && !row.get(houseIdx).isEmpty()) {
                String batchRef = row.get(houseIdx);
                Optional<ChickenBatch> batch = chickenBatchRepository.findByBatchNo(batchRef);
                if (batch.isEmpty()) {
                    batch = chickenBatchRepository.findByBatchName(batchRef);
                }
                batch.ifPresent(b -> sale.setBatchId(b.getId()));
            }
            
            // 销售日期
            Integer dateIdx = colIndex.get("date");
            if (dateIdx != null && dateIdx < row.size() && !row.get(dateIdx).isEmpty()) {
                sale.setSaleDate(parseDate(row.get(dateIdx)));
            }
            
            // 数量
            Integer qtyIdx = colIndex.get("quantity");
            if (qtyIdx != null && qtyIdx < row.size() && !row.get(qtyIdx).isEmpty()) {
                try { sale.setQuantity(Integer.parseInt(row.get(qtyIdx))); } catch (Exception ignored) {}
            }
            
            // 重量
            Integer weightIdx = colIndex.get("weight");
            if (weightIdx != null && weightIdx < row.size() && !row.get(weightIdx).isEmpty()) {
                try { sale.setWeight(new BigDecimal(row.get(weightIdx))); } catch (Exception ignored) {}
            }
            
            // 单价
            Integer priceIdx = colIndex.get("price");
            if (priceIdx != null && priceIdx < row.size() && !row.get(priceIdx).isEmpty()) {
                try { sale.setUnitPrice(new BigDecimal(row.get(priceIdx))); } catch (Exception ignored) {}
            }
            
            // 总金额
            Integer amountIdx = colIndex.get("amount");
            if (amountIdx != null && amountIdx < row.size() && !row.get(amountIdx).isEmpty()) {
                try { sale.setTotalAmount(new BigDecimal(row.get(amountIdx))); } catch (Exception ignored) {}
            }
            
            // 客户
            Integer customerIdx = colIndex.get("customer");
            if (customerIdx != null && customerIdx < row.size() && !row.get(customerIdx).isEmpty()) {
                String customerRef = row.get(customerIdx);
                Optional<Customer> customer = customerRepository.findByName(customerRef);
                if (customer.isEmpty()) {
                    customer = customerRepository.findByCustomerNo(customerRef);
                }
                customer.ifPresent(c -> sale.setCustomerId(c.getId()));
            }
            
            if (sale.getBatchId() != null && sale.getSaleDate() != null) {
                sales.add(sale);
            }
        }
        
        return sales;
    }

    private List<EggSale> parseEggSaleExcel(InputStream inputStream) throws IOException {
        List<EggSale> sales = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty()) continue;
            
            EggSale sale = new EggSale();
            if (row.size() > 1 && !row.get(1).isEmpty()) sale.setSaleNo(row.get(1));
            if (row.size() > 2 && !row.get(2).isEmpty()) sale.setSaleDate(parseDate(row.get(2)));
            
            // 产品：支持名称或编号
            if (row.size() > 3 && !row.get(3).isEmpty()) {
                String productRef = row.get(3);
                Optional<Product> product = productRepository.findByName(productRef);
                if (product.isEmpty()) {
                    product = productRepository.findByProductNo(productRef);
                }
                product.ifPresent(p -> sale.setProductId(p.getId()));
            }
            
            if (row.size() > 4 && !row.get(4).isEmpty()) sale.setSaleQuantity(Integer.parseInt(row.get(4)));
            if (row.size() > 5 && !row.get(5).isEmpty()) sale.setSaleWeight(new BigDecimal(row.get(5)));
            if (row.size() > 6 && !row.get(6).isEmpty()) sale.setUnitPrice(new BigDecimal(row.get(6)));
            if (row.size() > 7 && !row.get(7).isEmpty()) sale.setTotalAmount(new BigDecimal(row.get(7)));
            
            // 客户：支持名称或编号
            if (row.size() > 8 && !row.get(8).isEmpty()) {
                String customerRef = row.get(8);
                Optional<Customer> customer = customerRepository.findByName(customerRef);
                if (customer.isEmpty()) {
                    customer = customerRepository.findByCustomerNo(customerRef);
                }
                customer.ifPresent(c -> sale.setCustomerId(c.getId()));
            }
            
            if (sale.getProductId() != null && sale.getSaleDate() != null) {
                sales.add(sale);
            }
        }
        
        return sales;
    }

    private List<DeathRecord> parseDeathRecordExcel(InputStream inputStream) throws IOException {
        List<DeathRecord> records = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty()) continue;
            
            DeathRecord record = new DeathRecord();
            
            // 鸡舍：支持名称或编号
            if (row.size() > 1 && !row.get(1).isEmpty()) {
                String houseRef = row.get(1);
                Optional<ChickenHouse> house = chickenHouseRepository.findByName(houseRef);
                if (house.isEmpty()) {
                    house = chickenHouseRepository.findByHouseNo(houseRef);
                }
                house.ifPresent(h -> record.setHouseId(h.getId()));
            }
            
            if (row.size() > 2 && !row.get(2).isEmpty()) record.setDeathDate(parseDate(row.get(2)));
            if (row.size() > 3 && !row.get(3).isEmpty()) record.setDeathCount(Integer.parseInt(row.get(3)));
            if (row.size() > 4 && !row.get(4).isEmpty()) record.setDeathReason(row.get(4));
            
            if (record.getHouseId() != null && record.getDeathDate() != null && record.getDeathCount() != null) {
                // operatorId 默认设为1，实际应从当前登录用户获取
                record.setOperatorId(1L);
                records.add(record);
            }
        }
        
        return records;
    }

    private List<Inventory> parseInventoryExcel(InputStream inputStream) throws IOException {
        List<Inventory> inventories = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty()) continue;
            
            Inventory inventory = new Inventory();
            
            // 产品：支持名称或编号
            if (row.size() > 1 && !row.get(1).isEmpty()) {
                String productRef = row.get(1);
                Optional<Product> product = productRepository.findByName(productRef);
                if (product.isEmpty()) {
                    product = productRepository.findByProductNo(productRef);
                }
                product.ifPresent(p -> {
                    inventory.setProductId(p.getId());
                    inventory.setUnit(p.getUnit());
                });
            }
            
            // 鸡舍：支持名称或编号
            if (row.size() > 2 && !row.get(2).isEmpty()) {
                String houseRef = row.get(2);
                Optional<ChickenHouse> house = chickenHouseRepository.findByName(houseRef);
                if (house.isEmpty()) {
                    house = chickenHouseRepository.findByHouseNo(houseRef);
                }
                house.ifPresent(h -> inventory.setHouseId(h.getId()));
            }
            
            if (row.size() > 3 && !row.get(3).isEmpty()) inventory.setQuantity(new BigDecimal(row.get(3)));
            if (row.size() > 4 && !row.get(4).isEmpty()) inventory.setUnit(row.get(4));
            if (row.size() > 5 && !row.get(5).isEmpty()) inventory.setLocation(row.get(5));
            
            if (inventory.getProductId() != null && inventory.getQuantity() != null) {
                inventories.add(inventory);
            }
        }
        
        return inventories;
    }

    private List<FinanceRecord> parseFinanceRecordExcel(InputStream inputStream) throws IOException {
        List<FinanceRecord> records = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty()) continue;
            
            FinanceRecord record = new FinanceRecord();
            if (row.size() > 1 && !row.get(1).isEmpty()) record.setRecordNo(row.get(1));
            if (row.size() > 2 && !row.get(2).isEmpty()) record.setRecordDate(parseDate(row.get(2)));
            if (row.size() > 3 && !row.get(3).isEmpty()) record.setMoneyType(row.get(3)); // INCOME/COST
            if (row.size() > 4 && !row.get(4).isEmpty()) record.setAmount(new BigDecimal(row.get(4)));
            if (row.size() > 5 && !row.get(5).isEmpty()) record.setCostType(row.get(5));
            if (row.size() > 6 && !row.get(6).isEmpty()) record.setIncomeType(row.get(6));
            if (row.size() > 7 && !row.get(7).isEmpty()) record.setPaymentMethod(row.get(7));
            if (row.size() > 8 && !row.get(8).isEmpty()) record.setDescription(row.get(8));
            
            if (record.getRecordDate() != null && record.getAmount() != null) {
                records.add(record);
            }
        }
        
        return records;
    }

    private List<PurchaseOrder> parsePurchaseOrderExcel(InputStream inputStream) throws IOException {
        List<PurchaseOrder> orders = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty()) continue;
            
            PurchaseOrder order = new PurchaseOrder();
            if (row.size() > 1 && !row.get(1).isEmpty()) order.setOrderNo(row.get(1));
            
            // 供应商：支持名称或编号
            if (row.size() > 2 && !row.get(2).isEmpty()) {
                String supplierRef = row.get(2);
                Optional<Supplier> supplier = supplierRepository.findByName(supplierRef);
                if (supplier.isEmpty()) {
                    supplier = supplierRepository.findBySupplierNo(supplierRef);
                }
                supplier.ifPresent(s -> order.setSupplierId(s.getId()));
            }
            
            if (row.size() > 3 && !row.get(3).isEmpty()) order.setOrderDate(parseDate(row.get(3)));
            if (row.size() > 4 && !row.get(4).isEmpty()) order.setTotalAmount(new BigDecimal(row.get(4)));
            if (row.size() > 5 && !row.get(5).isEmpty()) order.setPaymentStatus(row.get(5)); // PAID/UNPAID
            if (row.size() > 6 && !row.get(6).isEmpty()) order.setDeliveryStatus(row.get(6)); // PENDING/COMPLETED
            if (row.size() > 7 && !row.get(7).isEmpty()) order.setRemarks(row.get(7));
            
            if (order.getSupplierId() != null && order.getOrderDate() != null) {
                // createdBy 默认设为1
                order.setCreatedBy(1L);
                if (order.getPaymentStatus() == null) order.setPaymentStatus("UNPAID");
                if (order.getDeliveryStatus() == null) order.setDeliveryStatus("PENDING");
                orders.add(order);
            }
        }
        
        return orders;
    }

    private List<SalesOrder> parseSalesOrderExcel(InputStream inputStream) throws IOException {
        List<SalesOrder> orders = new ArrayList<>();
        List<List<String>> data = ExcelUtils.readExcel(inputStream);
        
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.isEmpty()) continue;
            
            SalesOrder order = new SalesOrder();
            if (row.size() > 1 && !row.get(1).isEmpty()) order.setOrderNo(row.get(1));
            
            // 客户：支持名称或编号
            if (row.size() > 2 && !row.get(2).isEmpty()) {
                String customerRef = row.get(2);
                Optional<Customer> customer = customerRepository.findByName(customerRef);
                if (customer.isEmpty()) {
                    customer = customerRepository.findByCustomerNo(customerRef);
                }
                customer.ifPresent(c -> order.setCustomerId(c.getId()));
            }
            
            if (row.size() > 3 && !row.get(3).isEmpty()) order.setOrderDate(parseDateTime(row.get(3)));
            if (row.size() > 4 && !row.get(4).isEmpty()) order.setTotalAmount(new BigDecimal(row.get(4)));
            if (row.size() > 5 && !row.get(5).isEmpty()) order.setActualAmount(new BigDecimal(row.get(5)));
            if (row.size() > 6 && !row.get(6).isEmpty()) order.setPaymentStatus(row.get(6)); // PAID/UNPAID
            if (row.size() > 7 && !row.get(7).isEmpty()) order.setPaymentMethod(row.get(7));
            if (row.size() > 8 && !row.get(8).isEmpty()) order.setOrderStatus(row.get(8)); // PENDING/COMPLETED/CANCELLED
            
            if (order.getCustomerId() != null && order.getOrderDate() != null) {
                if (order.getPaymentStatus() == null) order.setPaymentStatus("UNPAID");
                if (order.getOrderStatus() == null) order.setOrderStatus("PENDING");
                if (order.getDiscountAmount() == null) order.setDiscountAmount(BigDecimal.ZERO);
                if (order.getDeliveryFee() == null) order.setDeliveryFee(BigDecimal.ZERO);
                orders.add(order);
            }
        }
        
        return orders;
    }

    // ==================== 工具方法 ====================

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            // 尝试多种日期格式
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
    
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return null;
        try {
            // 尝试多种日期时间格式
            DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            };
            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDateTime.parse(dateTimeStr.trim(), formatter);
                } catch (Exception ignored) {}
            }

            // 如果只有日期，补充时间为00:00:00
            LocalDate date = parseDate(dateTimeStr);
            if (date != null) {
                return date.atStartOfDay();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    private String normalizeNumericString(String value) {
                if (value == null || value.isEmpty()) return value;
                // Excel数字单元格读取后整数会变成 "1.0" 格式，需要去除末尾的 ".0"
                if (value.endsWith(".0")) {
                    String stripped = value.substring(0, value.length() - 2);
                    // 确保去除后确实是数字，避免误处理非数字字符串
                    try {
                        Long.parseLong(stripped);
                        return stripped;
                    } catch (NumberFormatException e) {
                        return value;
                    }
                }
                return value;
            }
}