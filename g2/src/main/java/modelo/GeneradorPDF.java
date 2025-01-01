package modelo;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeneradorPDF {
    /**
     * Genera un archivo PDF con los mensajes de un contacto.
     *
     * @param nombreContacto Nombre del contacto con quien se intercambiaron los mensajes.
     * @param mensajes       Lista de mensajes ordenados por fecha y hora.
     * @param rutaArchivo    Ruta donde se guardará el archivo PDF.
     * @throws IOException       Si ocurre un error al crear el archivo.
     * @throws DocumentException Si ocurre un error al generar el contenido del PDF.
     */
    public void generarPDF(String nombreContacto, List<String[]> mensajes, String rutaArchivo) throws IOException, DocumentException {
        // Crear el documento PDF
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));

        documento.open();

        // Agregar título
        Paragraph titulo = new Paragraph("Conversación con " + nombreContacto);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        documento.add(new Paragraph(" ")); // Espacio en blanco

        // Agregar mensajes
        for (String[] mensaje : mensajes) {
            String fechaHora = mensaje[0];
            String contenido = mensaje[1];
            String emisor = mensaje[2];

            documento.add(new Paragraph("[" + fechaHora + "] " + emisor + ": " + contenido));
        }

        documento.close();
    }
}
