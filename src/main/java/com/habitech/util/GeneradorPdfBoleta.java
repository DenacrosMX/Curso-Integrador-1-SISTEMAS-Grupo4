package com.habitech.util;

import com.habitech.model.Recibo;
import com.habitech.model.DetalleRecibo;
import com.habitech.model.Configuracion;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.OutputStream;
import java.awt.Color;
import java.text.SimpleDateFormat;

public class GeneradorPdfBoleta {

    public static void generarBoleta(Recibo recibo, Configuracion conf, OutputStream out) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, out);
        document.open();

        // Fuentes estilizadas para acabados profesionales
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
        Font fontSub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.GRAY);
        Font fontSeccion = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
        Font fontTextoBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
        Font fontTextoNormal = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

        // --- 1. CABECERA DEL COMPROBANTE ---
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);

        PdfPCell cellLeft = new PdfPCell();
        cellLeft.setBorder(Rectangle.NO_BORDER);
        cellLeft.addElement(new Paragraph(conf.getNombreCondominio() != null ? conf.getNombreCondominio().toUpperCase() : "HABITECH", fontTitulo));
        cellLeft.addElement(new Paragraph("Sistema de Gestión de Condominios", fontSub));
        headerTable.addCell(cellLeft);

        PdfPCell cellRight = new PdfPCell();
        cellRight.setBorder(Rectangle.NO_BORDER);
        cellRight.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph pComp = new Paragraph("BOLETA DE PAGO", fontTitulo);
        pComp.setAlignment(Element.ALIGN_RIGHT);
        Paragraph pNro = new Paragraph("Nro. Comprobante: " + recibo.getNroComprobante(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.RED));
        pNro.setAlignment(Element.ALIGN_RIGHT);
        cellRight.addElement(pComp);
        cellRight.addElement(pNro);
        headerTable.addCell(cellRight);
        document.add(headerTable);

        document.add(Chunk.NEWLINE);

        // --- 2. INFORMACIÓN DEL CONDOMINIO Y RESIDENTE ---
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(15);

        // Bloque Condominio
        PdfPCell c1 = new PdfPCell(new Paragraph("INFORMACIÓN DEL CONDOMINIO", fontSeccion));
        c1.setBackgroundColor(Color.GRAY);
        c1.setColspan(2);
        infoTable.addCell(c1);

        infoTable.addCell(new PdfPCell(new Paragraph("Condominio: " + conf.getNombreCondominio(), fontTextoNormal)));
        infoTable.addCell(new PdfPCell(new Paragraph("RUC: " + conf.getRuc(), fontTextoNormal)));
        infoTable.addCell(new PdfPCell(new Paragraph("Dirección: " + conf.getDireccion(), fontTextoNormal)));
        infoTable.addCell(new PdfPCell(new Paragraph("Fecha Emisión: " + new SimpleDateFormat("dd/MM/yyyy").format(recibo.getFechaEmision()), fontTextoNormal)));

        // Bloque Residente
        PdfPCell c2 = new PdfPCell(new Paragraph("DETALLES DEL RESIDENTE", fontSeccion));
        c2.setBackgroundColor(Color.GRAY);
        c2.setColspan(2);
        infoTable.addCell(c2);

        infoTable.addCell(new PdfPCell(new Paragraph("Residente: " + recibo.getNombreResidente(), fontTextoNormal)));
        infoTable.addCell(new PdfPCell(new Paragraph("Documento (DNI): " + recibo.getDniResidente(), fontTextoNormal)));
        infoTable.addCell(new PdfPCell(new Paragraph("Unidad Física: " + recibo.getDetalleUnidad(), fontTextoNormal)));
        infoTable.addCell(new PdfPCell(new Paragraph("Estado del Pago: " + recibo.getEstadoPago(), fontTextoBold)));

        document.add(infoTable);

        // --- 3. DETALLE DE CONCEPTOS COBRADOS ---
        PdfPTable tableDetalles = new PdfPTable(2);
        tableDetalles.setWidthPercentage(100);
        tableDetalles.setWidths(new float[]{75, 25});

        PdfPCell th1 = new PdfPCell(new Paragraph("Descripción del Concepto", fontSeccion));
        th1.setBackgroundColor(Color.DARK_GRAY);
        PdfPCell th2 = new PdfPCell(new Paragraph("Monto Total", fontSeccion));
        th2.setBackgroundColor(Color.DARK_GRAY);
        tableDetalles.addCell(th1);
        tableDetalles.addCell(th2);

        String nombreMes = obtenerNombreMes(recibo.getMesFacturado()) + " " + recibo.getAnioFacturado();
        for (DetalleRecibo det : recibo.getDetalles()) {
            tableDetalles.addCell(new PdfPCell(new Paragraph(det.getConceptoDescripcion() + " (" + nombreMes + ")", fontTextoNormal)));
            PdfPCell cMonto = new PdfPCell(new Paragraph("S/ " + det.getMontoIndividual().toString(), fontTextoNormal));
            cMonto.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableDetalles.addCell(cMonto);
        }

        // Totales finales
        PdfPCell cellTotalTxt = new PdfPCell(new Paragraph("Total Pagado:", fontTextoBold));
        cellTotalTxt.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tableDetalles.addCell(cellTotalTxt);

        PdfPCell cellTotalVal = new PdfPCell(new Paragraph("S/ " + recibo.getTotalAPagar().toString(), fontTextoBold));
        cellTotalVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tableDetalles.addCell(cellTotalVal);

        document.add(tableDetalles);
        document.add(Chunk.NEWLINE);

        // --- 4. DETALLES BANCARIOS VERIFICADOS ---
        if ("PAGADO".equals(recibo.getEstadoPago())) {
            PdfPTable bankTable = new PdfPTable(2);
            bankTable.setWidthPercentage(100);

            PdfPCell tc = new PdfPCell(new Paragraph("DETALLES DE LA TRANSACCIÓN BANCARIA", fontSeccion));
            tc.setBackgroundColor(Color.BLUE);
            tc.setColspan(2);
            bankTable.addCell(tc);

            bankTable.addCell(new PdfPCell(new Paragraph("Nro. Operación: " + recibo.getNroOperacion(), fontTextoNormal)));
            bankTable.addCell(new PdfPCell(new Paragraph("Medio de Pago: " + recibo.getMedioPago(), fontTextoNormal)));
            bankTable.addCell(new PdfPCell(new Paragraph("Fecha de Pago Registrada: " + new SimpleDateFormat("dd/MM/yyyy").format(recibo.getFechaPago()), fontTextoNormal)));
            bankTable.addCell(new PdfPCell(new Paragraph("Validado por Administración: SÍ ✓", fontTextoBold)));

            document.add(bankTable);
            document.add(Chunk.NEWLINE);
        }

        // --- 5. PIE DE PÁGINA REGLAMENTARIO ---
        Paragraph pie = new Paragraph("✓ Comprobante Emitido Electrónicamente\n" +
                "Este documento representa una constancia oficial de pago emitida automáticamente por el sistema Habitech tras la validación manual o emisión aprobada en el condominio.", FontFactory.getFont(FontFactory.HELVETICA, 8, Color.LIGHT_GRAY));
        pie.setAlignment(Element.ALIGN_CENTER);
        document.add(pie);

        document.close();
    }

    private static String obtenerNombreMes(int mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return (mes >= 1 && mes <= 12) ? meses[mes - 1] : "";
    }
}