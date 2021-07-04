package sample;
import Model.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
public class Controller implements Initializable {

    public Button delete;
    public Button NewLine;
    public Canvas canvas;
    public ColorPicker colorpik;
    public String flag;
    public Slider slider;
    public int selected; // 0 - Point, 1 - Rectangle, 2 - Triangle
    public Button nextShape;
    public Button previousShape;

    Model model;
    private GraphicsContext gr;

    Image Image;
    double X, Y, W = 100.0, H = 100.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model=new Model();
        gr = canvas.getGraphicsContext2D();
        selected = 0;
        initSlider();
    }

    public void initSlider() {
        slider.setMin(1);
        slider.setMax(10);
        slider.setValue(1);
        colorpik.setValue(Color.RED);
        flag =NewLine.getId();
    }

    public void update(Model model) {
        gr.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < model.getPointCount(); i++) {
            model.getShape(i).draw(gr);
        }
    }

    public void click_canvas(MouseEvent mouseEvent) { }

    public void save(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранение файла....");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Изображение", "*.png"),
                new FileChooser.ExtensionFilter("Изображение", "*.bmp")
        );
        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
        WritableImage wImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
        PixelWriter pw = wImage.getPixelWriter();
        for (int y = 0; y < (int)canvas.getHeight(); y++) {
            for (int x = 0; x < (int)canvas.getWidth(); x++){
                int index = model.searchShape(x,y);
                if (index < 0) {
                    pw.setColor(x, y, Color.TRANSPARENT);
                } else {
                    pw.setColor(x,y,model.getShape(index).getColor());
                }
            }
        }
        BufferedImage image = SwingFXUtils.fromFXImage(wImage, null);
        if (file != null) {
            ImageIO.write(image,"png", new FileOutputStream(file));
        }
    }

    public void load(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбрать ...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Картинка", "*.png"),
                new FileChooser.ExtensionFilter("Картинка", "*.bmp"));
        File loadImageFile = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (loadImageFile != null) {
            initDraw(gr,loadImageFile);
            update(model);
        }
    }

    private void initDraw(GraphicsContext gc,File file){
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        Image = new Image(file.toURI().toString());
        X = canvasWidth/2;
        Y = canvasHeight/2 ;
        gc.drawImage(Image, X, Y, W, H);

        PixelReader pixelReader = Image.getPixelReader();
        for (int y = 0; y < Image.getHeight(); y++) {
            for (int x = 0; x < Image.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                Point point =new Point(color, x,y, (int)slider.getValue());
                model.addPoint(point);
            }
        }
    }

    public void clean(ActionEvent actionEvent) {
        gr.clearRect(0,0,canvas.getHeight(),canvas.getWidth());
        model.deleteArray();
        update(model);
    }

    public void print(MouseEvent mouseEvent) {
        Shape shapes;
        System.out.println(selected);
        switch (selected){
            case 0 : shapes = new Point(colorpik.getValue(), (int)mouseEvent.getX(), (int) mouseEvent.getY(), (int)slider.getValue()); break;
            case 1 : shapes = new Rectangle(colorpik.getValue(), (int)mouseEvent.getX(), (int) mouseEvent.getY(), (int)slider.getValue()); break;
            case 2 : shapes = new Triangle(colorpik.getValue(), (int)mouseEvent.getX(), (int) mouseEvent.getY(), (int)slider.getValue()); break;
            default: return;
        }
        if (flag.equals(NewLine.getId())) {
            model.addPoint(shapes);
        } else {
            model.removePoint(shapes);
        }
        update(model);
    }

    public void m_eraser_but(ActionEvent actionEvent) {
        flag=NewLine.getId();
    }

    public void m_line(ActionEvent actionEvent) {
        flag=delete.getId();
    }

    public void toNextShape(ActionEvent actionEvent) {
        switch (selected) {
            case 0: selected = 1; break;
            case 1: selected = 2; break;
            case 2: selected = 0; break;
        }
    }

    public void toPreviousShape(ActionEvent actionEvent) {
        switch (selected) {
            case 0: selected = 2; break;
            case 1: selected = 0; break;
            case 2: selected = 1; break;
        }
    }
}