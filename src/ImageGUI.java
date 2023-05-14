import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import color_quantization_algorithms.*;

public class ImageGUI extends JFrame {
    private JLabel imageLabel;
    private JButton uniformButton, KmeansButton, loadImageButton, restoreOriginal,saveImageButton,compareButton,
    colorPaletteButton;
    int originalImageSize,kMeanImageSize,uniformImageSize;
    JFileChooser fileChooser = new JFileChooser();

    BufferedImage currentImage;
    JLabel kMeanLabel = new JLabel("clusters number");
    JLabel uniformLabel = new JLabel("colors number");
    JLabel colorPaletteLabel = new JLabel("colors palette number");
    private BufferedImage image;
    SpinnerModel kMeansSpinnerModel, uniformSpinnerModel;
    JSpinner kMeansSpinner,uniformSpinner;
    JPanel controlPanel;
    JPanel colorPalettePanel = new JPanel(new GridLayout(0, 5));
    JFrame colorPaletteFrame = new JFrame("Color Palette");



    public ImageGUI() {


        colorPaletteFrame.setSize(300, 200);

        // Set up the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setTitle("Image GUI");

        // Create the components
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Create the control panel
        controlPanel = new JPanel();
        // initialize buttons
        init_UniformButton();
        init_KmeansButton();
        init_KmeansSpinner();
        init_UniformSpinner();
        init_loadImageButton();
        init_saveImageButton();
        init_CompareButton();
        init_ColorPaletteButton();
        init_restoreOriginalImageButton();

        // Add the components to the frame
        add(imageLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(colorPalettePanel,BorderLayout.NORTH);
    }
    public void init_UniformButton() {
        uniformButton = new JButton("Uniform");
        uniformButton.addActionListener(e -> uniform());
        controlPanel.add(uniformButton);
    }
    public void init_ColorPaletteButton() {
        colorPaletteButton = new JButton("show color palette");
        colorPaletteButton.addActionListener(e -> showColorPalette());
        controlPanel.add(colorPaletteButton);
    }
    public void init_CompareButton() {
        compareButton = new JButton("compare algorithms");
        compareButton.addActionListener(e -> compareAlgorithms());
        controlPanel.add(compareButton);
    }
    public void init_loadImageButton() {
        loadImageButton = new JButton("load image");
        loadImageButton.addActionListener(e -> loadImage());
        controlPanel.add(loadImageButton);
    }
    public void init_saveImageButton() {
        saveImageButton = new JButton("save image");
        saveImageButton.addActionListener(e -> saveImage());
        controlPanel.add(saveImageButton);
    }
    public void init_KmeansButton() {
        KmeansButton = new JButton("K_Means");
        KmeansButton.addActionListener(e -> kMean());
        controlPanel.add(KmeansButton);
    }
    public void init_restoreOriginalImageButton() {
        restoreOriginal = new JButton("restore original");
        restoreOriginal.addActionListener(e -> restoreOriginal());
        controlPanel.add(restoreOriginal);
    }

    public void init_KmeansSpinner(){
        kMeansSpinnerModel = new SpinnerNumberModel(10, 1, 1024, 1);
        kMeansSpinner = new JSpinner(kMeansSpinnerModel);
        controlPanel.add(kMeanLabel);
        controlPanel.add(kMeansSpinner);
    }

    public void init_UniformSpinner(){

        uniformSpinnerModel = new SpinnerNumberModel(10, 1, 1024, 1);
        uniformSpinner = new JSpinner(uniformSpinnerModel);
        controlPanel.add(uniformLabel);
        controlPanel.add(uniformSpinner);
    }
    public void loadImage() {

        JFileChooser fileChooser = new JFileChooser("C:\\Users\\Jony\\Pictures\\image_processing");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                image = ImageIO.read(fileChooser.getSelectedFile());
                imageLabel.setIcon(new ImageIcon(image));
                originalImageSize=getImageSize(image);
                currentImage=image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void saveImage() {

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                ImageIO.write(currentImage,"png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void restoreOriginal()  {
        fileChooser.setSelectedFile(new File("original.png"));
        currentImage=image;
        imageLabel.setIcon(new ImageIcon(image));

    }
    private void uniform()  {
        fileChooser.setSelectedFile(new File("uniform.png"));
        BufferedImage quantizedImage = UniformQuantization.quantize(image,(int) uniformSpinnerModel.getValue());
        currentImage=quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));
        uniformImageSize=getImageSize(quantizedImage);

    }
    private void kMean() {
        fileChooser.setSelectedFile(new File("kMean.png"));
        BufferedImage quantizedImage = KMeansQuantizer.quantize(image, (int) kMeansSpinnerModel.getValue());
        currentImage=quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));
        kMeanImageSize=getImageSize(quantizedImage);
    }
    private Integer getImageSize(BufferedImage quantizedImage) {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        try {
            ImageIO.write(quantizedImage, "png", tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            tmp.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tmp.size()/1024;

    }
    private void compareAlgorithms() {
        kMean();
        uniform();
        restoreOriginal();
        int difference = Math.abs(kMeanImageSize-uniformImageSize);
        String theBetterAlgorithm="K_Means", theWorseAlgorithm="Uniform";
        if(kMeanImageSize>uniformImageSize){
            theBetterAlgorithm="Uniform";
            theWorseAlgorithm="K_Means";
        }
        JOptionPane.showMessageDialog
                (null, "the size of the original image is" +
                " "+originalImageSize+" kb"+"\n"+"the size of quantized image that use K_Means algorithm is"
                +" "+kMeanImageSize+" kb"+"\n"+"the size of quantized image that use Uniform algorithm is"
                +" "+uniformImageSize+" kb"+"\n"
                +theBetterAlgorithm+" is  better than "+theWorseAlgorithm+" by "+difference+" kb");

    }
    private void showColorPalette(){
        colorPalettePanel.removeAll();colorPaletteFrame.getContentPane().removeAll();
        ColorPalette colorPalette =new ColorPalette();
        List<Color> palette = colorPalette.createColorPalette(currentImage,10);
        for (Color color : palette) {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setBackground(color);

            colorPalettePanel.add(label);

            // Add the color palette panel to the frame


        }

        colorPaletteFrame.getContentPane().add(colorPalettePanel);
        colorPaletteFrame.setVisible(true);

    }

}