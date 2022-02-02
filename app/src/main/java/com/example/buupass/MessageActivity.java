package com.example.buupass;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MessageActivity extends AppCompatActivity {
    Button btn_create_pdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        btn_create_pdf = (Button) findViewById(R.id.btn_create_pdf);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        btn_create_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createPDFFile(Common.getAppPath(MessageActivity.this)+"test_pdf.pdf");
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    private void createPDFFile(String path) {
        if(new File(path).exists())
            new File(path).delete();
        try {
            Document document = new Document();
            //save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //open to write
            document.open();

            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Buupass");
            document.addCreator("Dennis Ochieng");

            //Font setting
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontsize = 20.0f;
            float valuefontsize = 26.0f;

            //custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);
            //Create Title of document
            Font titleFont = new Font(fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document, "Payment/Booking details", Element.ALIGN_CENTER,titleFont);
            // add more details
            Font orderNumberFont = new Font(fontName,fontsize,Font.NORMAL, colorAccent);
            addNewItem(document, "Booking No:",Element.ALIGN_LEFT,orderNumberFont);

            Font orderNumberValueFont = new Font(fontName,valuefontsize,Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "#717171",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeparator(document);

            addNewItem(document, "Booking Date",Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document,"2/2/2022", Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeparator(document);

            addNewItem(document, "Account Name",Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document,"Dennis Ochieng", Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeparator(document);

            // Add booking details
            addLineSpace(document);
            addNewItem(document, "Booking Detail", Element.ALIGN_CENTER, titleFont);
            addLineSeparator(document);

            //item 1
            addNewItemWithLeftAndRight(document,"Umoja 25", "(0.0%)",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"1*100", "100",titleFont,orderNumberValueFont);

            addLineSeparator(document);

            // item2
            addNewItemWithLeftAndRight(document,"Umoja 25", "(0.0%)",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"Total", "100",titleFont,orderNumberValueFont);

            document.close();

           //
            // Toast.makeText(this,"",Toast.L)

            addLineSeparator(document);

            //Total
            addLineSpace(document);
            addLineSpace(document);
            addNewItemWithLeftAndRight(document,"1*100", "100",titleFont,orderNumberValueFont);


            

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);

        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);


    }

    private void addLineSeparator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);

    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }
}