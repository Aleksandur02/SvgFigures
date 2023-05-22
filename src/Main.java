import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException, NoSuchFieldException, IllegalAccessException {
        int choice;
        int flag=0;
        int flagClose=0;
        String path = null;
        NodeList elements = null;
        ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
        ArrayList<Circle> circles = new ArrayList<Circle>();
        ArrayList<Lines> line = new ArrayList<Lines>();
        ArrayList<Rectangle> rectangles1= new ArrayList<Rectangle>();
        ArrayList<Circle> circles1 = new ArrayList<Circle>();
        ArrayList<Lines> lines = new ArrayList<Lines>();
        ArrayList<Integer> integers = new ArrayList<Integer>();

        do {
            choice = Menu.menu();

            switch (choice) {
                case 1:
                    if (flag == 0) {
                        System.out.print("File path:");
                        Scanner scanner = new Scanner(System.in);
                        path = scanner.next();
                        boolean endsWithSvg = path.endsWith(".svg");
                        if (endsWithSvg) {
                            elements = Functionality.open(path);
                            if (elements != null) {
                                Functionality.load(elements, rectangles, circles, line);
                                flag = 1;
                                flagClose=0;
                                System.out.println("Successfully opened " + path);
                            }

                        } else {
                            System.out.println("Invalid file format");
                        }

                    } else {
                        System.out.println("File already has been opened");
                    }
                    break;

                case 2:
                    if (flag == 1&&flagClose==0) {
                        System.out.println("Which figure you want to be created");
                        Scanner scan = new Scanner(System.in);
                        String figure = scan.next();
                        switch (figure) {
                            case "Circle":
                                Functionality.createCircle(circles, circles1);
                                System.out.println("Successfully created circle");
                                break;
                            case "Rectangle":
                                Functionality.createRectangle(rectangles, rectangles1);
                                System.out.println("Successfully created rectangle");
                                break;
                            case "Line":
                                Functionality.createLine(line, lines);
                                System.out.println("Successfully created line");
                                break;
                            default:
                                System.out.println("Invalid input");
                        }
                    } else {
                        System.out.println("File is not opened yet or it's closed");
                    }
                    break;

                case 3:

                    if (flag == 1&&flagClose==0) {
                        System.out.println("Print:");
                        Functionality.print(rectangles, circles, line);
                    } else {
                        System.out.println("File is not opened yet or it's closed");
                    }
                    break;
                case 4:

                    if (flag == 1&&flagClose==0) {
                        Functionality.save(path, circles1, rectangles1, lines);
                        Functionality.removeFile(path,integers);
                        System.out.println("Successfully saved the changes to " + path);
                    }else {
                        System.out.println("File is not opened yet or it's closed");
                    }
                    break;

                case 5:
                    if (flag == 1&&flagClose==0) {
                        Scanner scanner = new Scanner(System.in);
                        System.out.print("Enter path:");
                        String saveAs = scanner.next();
                        boolean endsWithSvg = saveAs.endsWith(".svg");
                        if (endsWithSvg) {
                            Functionality.saveAs(saveAs, circles, rectangles, line);
                        } else {
                            System.out.println("Invalid file format");
                        }
                    }else {
                        System.out.println("File is not opened yet or it's closed");
                    }

                    break;

                case 6:
                    if(flagClose==1) {
                        System.out.println("File is closed");
                    }else{
                        System.out.println("The following commands are supported: \n" +
                                "open <file> opens <file> \n" +
                                "close closes currently opened file \n" +
                                "save saves the currently open file \n" +
                                "save as <file> saves the currently open file in <file> \n" +
                                "help prints this information \n" +
                                "exit exists the program ");
                    }
                    break;
                case 7:
                    if(flag==1) {
                        flagClose = 1;
                        flag = 0;
                        System.out.println("File has been closed");
                        circles1.clear();
                        circles.clear();
                        rectangles.clear();
                        rectangles1.clear();
                        lines.clear();
                        line.clear();
                    }else {
                        System.out.println("File is not open");
                    }
                    break;
                case 8:
                    if (flag == 1&&flagClose==0) {
                        Scanner scanner = new Scanner(System.in);
                        System.out.print("Enter the index of the figure to be removed:");
                        int index = scanner.nextInt();
                        Functionality.remove(index, rectangles, circles, line, integers);
                        System.out.println("Successfully Erased");
                    }else {
                        System.out.println("File is not opened yet or it's closed");
                    }
            }

        }while (choice != 9) ;

    }

}