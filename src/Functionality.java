import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.batik.util.XMLResourceDescriptor;

import javax.sound.sampled.Line;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

public class Functionality {
    public static int index=1;
    public static NodeList open(String path) throws IOException, ParserConfigurationException, TransformerException {
        File svgFile1 = new File(path);
        if (svgFile1.exists()) {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            String svgFile = new File(path).toURI().toString();
            Document doc = factory.createDocument(svgFile);
            NodeList elements = doc.getElementsByTagName("*");
            return elements;}
        else{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element svg = doc.createElementNS("http://www.w3.org/2000/svg", "svg");
            doc.appendChild(svg);

            // Запис на SVG файла
            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(svgFile1);
            transformer.transform(source, result);

            System.out.println("Новият празен SVG файл е създаден успешно.");
            return null;
        }
    }

    public static void load(NodeList figureNodes, ArrayList<Rectangle> rectangles, ArrayList<Circle> circles, ArrayList<Lines> lines) throws ParserConfigurationException, IOException, SAXException {
        for (int i = 0; i < figureNodes.getLength(); i++) {
            Node node = figureNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element figureElement = (Element) node;
                // Check if the element is a rectangle
                if ("rect".equals(figureElement.getTagName())) {
                    // Extract attributes of the rectangle element
                    float x = Float.parseFloat(figureElement.getAttribute("x"));
                    float y = Float.parseFloat(figureElement.getAttribute("y"));
                    float width = Float.parseFloat(figureElement.getAttribute("width"));
                    float height = Float.parseFloat(figureElement.getAttribute("height"));
                    String color = figureElement.getAttribute("fill");
                    Rectangle rect = new Rectangle(x, y, width, height, color);
                    rect.setIndex(index);
                    index=index+1;
                    rectangles.add(rect);
                }
                if ("circle".equals(figureElement.getTagName())) {
                    float cx = Float.parseFloat(figureElement.getAttribute("cx"));
                    float cy = Float.parseFloat(figureElement.getAttribute("cy"));
                    float r = Float.parseFloat(figureElement.getAttribute("r"));
                    String fill = figureElement.getAttribute("fill");
                    Circle c = new Circle(cx, cy, r, fill);
                    c.setIndex(index);
                    index=index+1;
                    circles.add(c);
                }
                if ("line".equals(figureElement.getTagName())) {
                    float x1 = Float.parseFloat(figureElement.getAttribute("x1"));
                    float y1 = Float.parseFloat(figureElement.getAttribute("y1"));
                    float x2 = Float.parseFloat(figureElement.getAttribute("x2"));
                    float y2 = Float.parseFloat(figureElement.getAttribute("y2"));
                    String stroke = figureElement.getAttribute("style");
                    Lines line=new Lines(x1,y1,x2,y2,stroke);
                    line.setIndex(index);
                    index=index+1;
                    lines.add(line);


                }


            }
        }
    }

    public static void print(ArrayList<Rectangle> rectangles, ArrayList<Circle> circles, ArrayList<Lines> lines) {
        for(int j=1;j<index;j++) {
            for (Rectangle i : rectangles) {
                if(i.getIndex()==j)
                {
                    System.out.print(i.toString());
                }
            }
            for (Circle i : circles) {
                if(i.getIndex()==j) {
                    System.out.print(i.toString());
                }
            }
            for (Lines i : lines) {
                if(i.getIndex()==j){
                    System.out.print(i.toString());
                }
            }

            System.out.println();
        }
    }

    public static void remove(int index, ArrayList<Rectangle> rectangles, ArrayList<Circle> circles, ArrayList<Lines> lines, ArrayList<Integer> integers ) {
        boolean removed = false;

        for (int i = 0; i < rectangles.size(); i++) {
            if (rectangles.get(i).getIndex() == index) {
                rectangles.remove(i);
                integers.add(index);
                removed = true;
                break;
            }
        }

        if (removed==false) {
            for (int i = 0; i < circles.size(); i++) {
                if (circles.get(i).getIndex() == index) {
                    circles.remove(i);
                    integers.add(index);
                    removed = true;
                    break;
                }
            }
        }

        if (removed==false) {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).getIndex() == index) {
                    lines.remove(i);
                    integers.add(index);
                    break;
                }
            }
        }
    }
    public static void removeFile(String path,ArrayList<Integer> integers) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(path));

        // Get the list of all shape elements
        NodeList shapes = doc.getDocumentElement().getElementsByTagName("*");

        for (int i=0;i<integers.size();i++) {
            Node shape = shapes.item(integers.get(i) - 1);
            shape.getParentNode().removeChild(shape);
        }
        // Output the modified SVG file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(path));
        transformer.transform(source, result);
    }

    public static void createCircle(ArrayList<Circle> circles,ArrayList<Circle> circles1) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("cx:");
        float cx = scanner.nextFloat();
        System.out.print("cy:");
        float cy = scanner.nextFloat();
        System.out.print("r:");
        float r = scanner.nextFloat();
        System.out.print("color:");
        String color = scanner.next();
        Circle c = new Circle(cx, cy, r, color);
        c.setIndex(index);
        index=index+1;
        circles.add(c);
        circles1.add(c);


    }

    public static void createRectangle(ArrayList<Rectangle> rectangles,ArrayList<Rectangle> rectangles1) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("x:");
        float x = scanner.nextFloat();
        System.out.print("y:");
        float y = scanner.nextFloat();
        System.out.print("width:");
        float width = scanner.nextFloat();
        System.out.print("height:");
        float height = scanner.nextFloat();
        System.out.print("color:");
        String color = scanner.next();
        Rectangle r = new Rectangle(x, y, width, height, color);
        r.setIndex(index);
        index=index+1;
        rectangles.add(r);
        rectangles1.add(r);
    }
    public static void createLine(ArrayList<Lines> lines,ArrayList<Lines> lines1) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("x1:");
        float x1 = scanner.nextFloat();
        System.out.print("y1:");
        float y1 = scanner.nextFloat();
        System.out.print("x2:");
        float x2 = scanner.nextFloat();
        System.out.print("y2:");
        float y2 = scanner.nextFloat();
        System.out.print("stroke:");
        String stroke = scanner.next();
        Lines line=new Lines(x1,y1,x2,y2,stroke);
        line.setIndex(index);
        index=index+1;
        lines.add(line);
        lines1.add(line);


    }


    public static void save(String path, ArrayList<Circle> c,ArrayList<Rectangle> r,ArrayList<Lines> l) throws ParserConfigurationException, IOException, SAXException, TransformerException, NoSuchFieldException, IllegalAccessException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse((path));
        if(c.size()>0&&r.size()>0&&l.size()>0) {
            int smallestC = c.get(0).getIndex();
            int smallestR = r.get(0).getIndex();
            int smallestL = l.get(0).getIndex();
            int biggestC = c.get(0).getIndex();
            int biggestR = r.get(0).getIndex();
            int biggestL = l.get(0).getIndex();
            int smallest;
            int biggest;

            for (int i = 0; i < c.size(); i++) {
                if (c.get(i).getIndex() < smallestC) {
                    smallestC = c.get(i).getIndex();
                }
            }


            for (int i = 0; i < r.size(); i++) {
                if (r.get(i).getIndex() < smallestR) {
                    smallestR = r.get(i).getIndex();
                }
            }


            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getIndex() < smallestL) {
                    smallestL = l.get(i).getIndex();
                }
            }


            for (int i = 0; i < c.size(); i++) {
                if (c.get(i).getIndex() > biggestC) {
                    biggestC = c.get(i).getIndex();
                }
            }


            for (int i = 0; i < r.size(); i++) {
                if (r.get(i).getIndex() > biggestR) {
                    biggestR = r.get(i).getIndex();
                }
            }


            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getIndex() > biggestL) {
                    biggestL = l.get(i).getIndex();
                }
            }

            if (smallestL <= smallestC && smallestL <= smallestR) {
                smallest = smallestL;
            } else if (smallestC <= smallestL && smallestC <= smallestR) {
                smallest = smallestC;
            } else {
                smallest = smallestR;
            }
            if (biggestL >= biggestC && biggestL >= biggestR) {
                biggest = biggestL;
            } else if (biggestC >= biggestL && biggestC >= biggestR) {
                biggest = biggestC;
            } else {
                biggest = biggestR;
            }
            for (int i = smallest; i <= biggest; i++) {
                for (Circle k : c) {
                    if (k.getIndex() == i) {
                        Element circle = doc.createElementNS("http://www.w3.org/2000/svg", "circle");
                        circle.setAttribute("cx", Float.toString(k.getCx()));
                        circle.setAttribute("cy", Float.toString(k.getCy()));
                        circle.setAttribute("r", Float.toString(k.getR()));
                        circle.setAttribute("fill", k.getColor());
                        doc.getDocumentElement().appendChild(circle);

                        // Записване на SVG файл
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult((path));
                        transformer.transform(source, result);
                    }
                }
                for (Lines k : l) {
                    if (k.getIndex() == i) {
                        Element line = doc.createElementNS("http://www.w3.org/2000/svg", "line");
                        line.setAttribute("x1", Float.toString(k.getX1()));
                        line.setAttribute("y1", Float.toString(k.getY1()));
                        line.setAttribute("x2", Float.toString(k.getX2()));
                        line.setAttribute("y2", Float.toString(k.getY2()));
                        Color color = (Color) Color.class.getField(k.getColor().toLowerCase()).get(null);
                        line.setAttribute("style", "stroke:" + String.format("rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue()) + ";stroke-width:2");

                        doc.getDocumentElement().appendChild(line);

                        // Записване на SVG файл
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult((path));
                        transformer.transform(source, result);
                    }
                }
                for (Rectangle k : r) {
                    if (k.getIndex() == i) {
                        Element rectangle = doc.createElementNS("http://www.w3.org/2000/svg", "rect");
                        rectangle.setAttribute("x", Float.toString(k.getX()));
                        rectangle.setAttribute("y", Float.toString(k.getY()));
                        rectangle.setAttribute("width", Float.toString(k.getWidth()));
                        rectangle.setAttribute("height", Float.toString(k.getHeight()));
                        rectangle.setAttribute("fill", k.getColor());
                        doc.getDocumentElement().appendChild(rectangle);

                        // Записване на SVG файл
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult((path));
                        transformer.transform(source, result);
                    }
                }


            }
        }
        if(c.size()>0&&r.size()>0&&l.isEmpty()){
            int smallestC = c.get(0).getIndex();
            int smallestR = r.get(0).getIndex();
            int biggestC = c.get(0).getIndex();
            int biggestR = r.get(0).getIndex();
            int smallest;
            int biggest;
            for (int i = 0; i < c.size(); i++) {
                if (c.get(i).getIndex() < smallestC) {
                    smallestC = c.get(i).getIndex();
                }
            }


            for (int i = 0; i < r.size(); i++) {
                if (r.get(i).getIndex() < smallestR) {
                    smallestR = r.get(i).getIndex();
                }
            }

            for (int i = 0; i < c.size(); i++) {
                if (c.get(i).getIndex() > biggestC) {
                    biggestC = c.get(i).getIndex();
                }
            }


            for (int i = 0; i < r.size(); i++) {
                if (r.get(i).getIndex() > biggestR) {
                    biggestR = r.get(i).getIndex();
                }
            }

            if(smallestC<smallestR){
                smallest=smallestC;
            }else{
                smallest=smallestR;
            }

            if(biggestC>biggestR){
                biggest=biggestC;
            }else{
                biggest=biggestR;
            }
            for (int i = smallest; i <= biggest; i++) {
                for (Circle k : c) {
                    if (k.getIndex() == i) {
                        Element circle = doc.createElementNS("http://www.w3.org/2000/svg", "circle");
                        circle.setAttribute("cx", Float.toString(k.getCx()));
                        circle.setAttribute("cy", Float.toString(k.getCy()));
                        circle.setAttribute("r", Float.toString(k.getR()));
                        circle.setAttribute("fill", k.getColor());
                        doc.getDocumentElement().appendChild(circle);

                        // Записване на SVG файл
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult((path));
                        transformer.transform(source, result);
                    }
                }
                for (Rectangle k : r) {
                    if (k.getIndex() == i) {
                        Element rectangle = doc.createElementNS("http://www.w3.org/2000/svg", "rect");
                        rectangle.setAttribute("x", Float.toString(k.getX()));
                        rectangle.setAttribute("y", Float.toString(k.getY()));
                        rectangle.setAttribute("width", Float.toString(k.getWidth()));
                        rectangle.setAttribute("height", Float.toString(k.getHeight()));
                        rectangle.setAttribute("fill", k.getColor());
                        doc.getDocumentElement().appendChild(rectangle);

                        // Записване на SVG файл
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult((path));
                        transformer.transform(source, result);
                    }
                }


            }



        }
        if(c.size()>0&&l.size()>0&&r.isEmpty()){
            int smallestC = c.get(0).getIndex();
            int smallestL = l.get(0).getIndex();
            int biggestC = c.get(0).getIndex();
            int biggestL = l.get(0).getIndex();
            int smallest;
            int biggest;
            for (int i = 0; i < c.size(); i++) {
                if (c.get(i).getIndex() < smallestC) {
                    smallestC = c.get(i).getIndex();
                }
            }


            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getIndex() < smallestL) {
                    smallestL = l.get(i).getIndex();
                }
            }

            for (int i = 0; i < c.size(); i++) {
                if (c.get(i).getIndex() > biggestC) {
                    biggestC = c.get(i).getIndex();
                }
            }


            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getIndex() > biggestL) {
                    biggestL = l.get(i).getIndex();
                }
            }

            if(smallestC<smallestL){
                smallest=smallestC;
            }else{
                smallest=smallestL;
            }

            if(biggestC>biggestL){
                biggest=biggestC;
            }else{
                biggest=biggestL;
            }
            for (int i = smallest; i <= biggest; i++) {
                for (Circle k : c) {
                    if (k.getIndex() == i) {
                        Element circle = doc.createElementNS("http://www.w3.org/2000/svg", "circle");
                        circle.setAttribute("cx", Float.toString(k.getCx()));
                        circle.setAttribute("cy", Float.toString(k.getCy()));
                        circle.setAttribute("r", Float.toString(k.getR()));
                        circle.setAttribute("fill", k.getColor());
                        doc.getDocumentElement().appendChild(circle);

                        // Записване на SVG файл
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult((path));
                        transformer.transform(source, result);
                    }
                }
                for (Lines k : l) {
                    if (k.getIndex() == i) {
                        Element line = doc.createElementNS("http://www.w3.org/2000/svg", "line");
                        line.setAttribute("x1", Float.toString(k.getX1()));
                        line.setAttribute("y1", Float.toString(k.getY1()));
                        line.setAttribute("x2", Float.toString(k.getX2()));
                        line.setAttribute("y2", Float.toString(k.getY2()));
                        Color color = (Color) Color.class.getField(k.getColor().toLowerCase()).get(null);
                        line.setAttribute("style", "stroke:" + String.format("rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue()) + ";stroke-width:2");

                        doc.getDocumentElement().appendChild(line);

                        // Записване на SVG файл
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult((path));
                        transformer.transform(source, result);
                    }
                }


            }



        }
        if(r.size()>0&&l.size()>0&&c.isEmpty()){
            int smallestR = r.get(0).getIndex();
            int smallestL = l.get(0).getIndex();
            int biggestR = r.get(0).getIndex();
            int biggestL = l.get(0).getIndex();
            int smallest;
            int biggest;
            for (int i = 0; i < r.size(); i++) {
                if (r.get(i).getIndex() < smallestR) {
                    smallestR = r.get(i).getIndex();
                }
            }


            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getIndex() < smallestL) {
                    smallestL = l.get(i).getIndex();
                }
            }

            for (int i = 0; i < r.size(); i++) {
                if (r.get(i).getIndex() > biggestR) {
                    biggestR = c.get(i).getIndex();
                }
            }


            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getIndex() > biggestL) {
                    biggestL = l.get(i).getIndex();
                }
            }

            if(smallestR<smallestL){
                smallest=smallestR;
            }else{
                smallest=smallestL;
            }

            if(biggestR>biggestL){
                biggest=biggestR;
            }else{
                biggest=biggestL;
            }
            for (int i = smallest; i <= biggest; i++) {
                for (Rectangle k : r) {
                    if (k.getIndex() == i) {
                        Element rectangle = doc.createElementNS("http://www.w3.org/2000/svg", "rect");
                        rectangle.setAttribute("x", Float.toString(k.getX()));
                        rectangle.setAttribute("y", Float.toString(k.getY()));
                        rectangle.setAttribute("width", Float.toString(k.getWidth()));
                        rectangle.setAttribute("height", Float.toString(k.getHeight()));
                        rectangle.setAttribute("fill", k.getColor());
                        doc.getDocumentElement().appendChild(rectangle);

                        // Записване на SVG файл
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult((path));
                        transformer.transform(source, result);
                    }
                }
                for (Lines k : l) {
                    if (k.getIndex() == i) {
                        Element line = doc.createElementNS("http://www.w3.org/2000/svg", "line");
                        line.setAttribute("x1", Float.toString(k.getX1()));
                        line.setAttribute("y1", Float.toString(k.getY1()));
                        line.setAttribute("x2", Float.toString(k.getX2()));
                        line.setAttribute("y2", Float.toString(k.getY2()));
                        Color color = (Color) Color.class.getField(k.getColor().toLowerCase()).get(null);
                        line.setAttribute("style", "stroke:" + String.format("rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue()) + ";stroke-width:2");

                        doc.getDocumentElement().appendChild(line);

                        // Записване на SVG файл
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult((path));
                        transformer.transform(source, result);
                    }
                }


            }

        }

        if(c.size()>0&&r.isEmpty()&&l.isEmpty()){
            for (Circle k : c) {
                Element circle = doc.createElementNS("http://www.w3.org/2000/svg", "circle");
                circle.setAttribute("cx", Float.toString(k.getCx()));
                circle.setAttribute("cy", Float.toString(k.getCy()));
                circle.setAttribute("r", Float.toString(k.getR()));
                circle.setAttribute("fill", k.getColor());
                doc.getDocumentElement().appendChild(circle);

                // Записване на SVG файл
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult((path));
                transformer.transform(source, result);

            }
        }

        if(r.size()>0&&c.isEmpty()&&l.isEmpty()) {
            for (Rectangle k : r) {

                Element rectangle = doc.createElementNS("http://www.w3.org/2000/svg", "rect");
                rectangle.setAttribute("x", Float.toString(k.getX()));
                rectangle.setAttribute("y", Float.toString(k.getY()));
                rectangle.setAttribute("width", Float.toString(k.getWidth()));
                rectangle.setAttribute("height", Float.toString(k.getHeight()));
                rectangle.setAttribute("fill", k.getColor());
                doc.getDocumentElement().appendChild(rectangle);

                // Записване на SVG файл
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult((path));
                transformer.transform(source, result);

            }
        }

        if(l.size()>0&&c.isEmpty()&&r.isEmpty()) {

            for (Lines k : l) {

                Element line = doc.createElementNS("http://www.w3.org/2000/svg", "line");
                line.setAttribute("x1", Float.toString(k.getX1()));
                line.setAttribute("y1", Float.toString(k.getY1()));
                line.setAttribute("x2", Float.toString(k.getX2()));
                line.setAttribute("y2", Float.toString(k.getY2()));
                Color color = (Color) Color.class.getField(k.getColor().toLowerCase()).get(null);
                line.setAttribute("style", "stroke:" + String.format("rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue()) + ";stroke-width:2");

                doc.getDocumentElement().appendChild(line);

                // Записване на SVG файл
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult((path));
                transformer.transform(source, result);

            }
        }

    }




    public static void saveAs(String path, ArrayList<Circle> c, ArrayList<Rectangle> r, ArrayList<Lines> l) throws ParserConfigurationException, TransformerException, IOException, SAXException, NoSuchFieldException, IllegalAccessException {
        File svgFile = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element svg = doc.createElementNS("http://www.w3.org/2000/svg", "svg");
        doc.appendChild(svg);

        // Запис на празния SVG файл
        javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
        javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(svgFile);
        transformer.transform(source, result);

        Document doc2 = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(svgFile);
        Element svgRoot = doc2.getDocumentElement();

        for (Circle circle : c) {
            Element circleElement = doc2.createElementNS("http://www.w3.org/2000/svg", "circle");
            circleElement.setAttribute("cx", Float.toString(circle.getCx()));
            circleElement.setAttribute("cy", Float.toString(circle.getCy()));
            circleElement.setAttribute("r", Float.toString(circle.getR()));
            circleElement.setAttribute("fill", circle.getColor());
            svgRoot.appendChild(circleElement);
        }

        for (Rectangle rectangle : r) {
            Element rectangleElement = doc2.createElementNS("http://www.w3.org/2000/svg", "rect");
            rectangleElement.setAttribute("x", Float.toString(rectangle.getX()));
            rectangleElement.setAttribute("y", Float.toString(rectangle.getY()));
            rectangleElement.setAttribute("width", Float.toString(rectangle.getWidth()));
            rectangleElement.setAttribute("height", Float.toString(rectangle.getHeight()));
            rectangleElement.setAttribute("fill", rectangle.getColor());
            svgRoot.appendChild(rectangleElement);
        }

        for (Lines line : l) {
            Element lineElement = doc2.createElementNS("http://www.w3.org/2000/svg", "line");
            lineElement.setAttribute("x1", Float.toString(line.getX1()));
            lineElement.setAttribute("y1", Float.toString(line.getY1()));
            lineElement.setAttribute("x2", Float.toString(line.getX2()));
            lineElement.setAttribute("y2", Float.toString(line.getY2()));
            lineElement.setAttribute("stroke", line.getColor());
            svgRoot.appendChild(lineElement);
        }

        // Запис на променения SVG файл
        javax.xml.transform.Transformer transformer2 = transformerFactory.newTransformer();
        javax.xml.transform.dom.DOMSource source2 = new javax.xml.transform.dom.DOMSource(doc2);
        javax.xml.transform.stream.StreamResult result2 = new javax.xml.transform.stream.StreamResult(svgFile);
        transformer2.transform(source2, result2);

        System.out.println("SVG файлът е създаден");
    }


}
