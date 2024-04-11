import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Static utility class that is responsible for transforming the images.
 * Each function (or at least most functions) take in an Image and return
 * a transformed image.
 */
public class ImageManipulator {
    /**
     * Loads the image at the given path
     * @param path path to image to load
     * @return an Img object that has the given image loaded
     * @throws IOException
     */
    public static Img LoadImage(String path) throws IOException {
        if(new File(path).isFile()){
            Img image = new Img(path);
            return image;
        }
        throw new IOException("path does not go to an image");

    }

    /**
     * Saves the image to the given file location
     * @param image image to save
     * @param path location in file system to save the image
     * @throws IOException
     */
    public static void SaveImage(Img image, String path) throws IOException {
        image.Save("png", path);
    }

    /**
     * Converts the given image to grayscale (black, white, and gray). This is done
     * by finding the average of the RGB channel values of each pixel and setting
     * each channel to the average value.
     * @param image image to transform
     * @return the image transformed to grayscale
     */
    public static Img ConvertToGrayScale(Img image) {
       int y = image.GetHeight();
       int x = image.GetWidth();
        for(int c=0; c<y; c++){
           for(int r=0; r<x; r++){
               RGB pixel = image.GetRGB(r, c);
               int average = (pixel.GetRed()+pixel.GetGreen()+pixel.GetBlue())/3;
               RGB change = new RGB(average, average, average);
               image.SetRGB(r, c, change);
           }
       }
       return image;
    }

    /**
     * Inverts the image. To invert the image, for each channel of each pixel, we get
     * its new value by subtracting its current value from 255. (r = 255 - r)
     * @param image image to transform
     * @return image transformed to inverted image
     */
    public static Img InvertImage(Img image) {
        int y = image.GetHeight();
        int x = image.GetWidth();
        for(int c=0; c<y; c++){
            for(int r=0; r<x; r++){
                RGB pixel = image.GetRGB(r, c);
                RGB change = new RGB(255-pixel.GetRed(), 255-pixel.GetGreen(), 255-pixel.GetBlue());
                image.SetRGB(r, c, change);
            }
        }

        return image;
    }

    /**
     * Converts the image to sepia. To do so, for each pixel, we use the following equations
     * to get the new channel values:
     * r = .393r + .769g + .189b
     * g = .349r + .686g + .168b
     * b = 272r + .534g + .131b
     * @param image image to transform
     * @return image transformed to sepia
     */
    public static Img ConvertToSepia(Img image) throws IOException {
        int y = image.GetHeight();
        int x = image.GetWidth();
        Img newImage = new Img(x, y);
        for(int row=0; row<x; row++){
            for(int c=0; c<y; c++){
                RGB pixel = image.GetRGB(row, c);
                int r = pixel.GetRed();
                int g = pixel.GetGreen();
                int b = pixel.GetBlue();
                int newRed = (int)((0.393*r) + (0.769*g) + (0.189*b));
                int newGreen = (int)((0.349*r) + (0.686 * g) + (0.168 * b));
                int newBlue = (int)((0.272 * r) + (0.534 * g) + (0.131 * b));
                RGB change = new RGB(newRed, newGreen, newBlue);
                newImage.SetRGB(r, c, change);
            }
        }
        SaveImage(newImage, "/Users/dinatsang/Desktop/Music/sepia.png");
        return newImage;
    }

    /**
     * Creates a stylized Black/White image (no gray) from the given image. To do so:
     * 1) calculate the luminance for each pixel. Luminance = (.299 r^2 + .587 g^2 + .114 b^2)^(1/2)
     * 2) find the median luminance
     * 3) each pixel that has luminance >= median_luminance will be white changed to white and each pixel
     *      that has luminance < median_luminance will be changed to black
     * @param image image to transform
     * @return black/white stylized form of image
     */
    public static Img ConvertToBW(Img image) throws IOException {
        int y = image.GetHeight();
        int x = image.GetWidth();
        double median;
        ArrayList<Double> lumens = new ArrayList<>();
        for(int c=0; c<y; c++) {
            for (int r = 0; r < x; r++) {
                RGB pixel = image.GetRGB(r, c);
                double lumen = Math.sqrt((0.299 * Math.pow(pixel.GetRed(), 2)) + (0.587 * Math.pow(pixel.GetGreen(), 2)) + (0.114 * Math.pow((pixel.GetBlue()), 2)));
                lumens.add(lumen);
            }
        }
        lumens.sort(Comparator.naturalOrder());
        if(lumens.size()%2 == 1){
            median = lumens.get(1+(lumens.size()/2));
        }
        else {
            double a = lumens.get(lumens.size()/2);
            double b = lumens.get(1+(lumens.size()/2));
            median = (a+b)/2;
        }
        for(int c=0; c<y; c++){
            for(int r=0; r<x; r++){
                RGB pixel = image.GetRGB(r, c);
                double lumen = Math.sqrt((0.299 * Math.pow(pixel.GetRed(), 2)) + (0.587 * Math.pow(pixel.GetGreen(), 2)) + (0.114 * Math.pow((pixel.GetBlue()), 2)));
                if(lumen>median){
                    RGB white = new RGB(255, 255, 255);
                    image.SetRGB(r, c, white);
                }
                else{
                    RGB black = new RGB(0, 0, 0);
                    image.SetRGB(r, c, black);
                }
            }
        }
        SaveImage(image, "/Users/dinatsang/Desktop/Music/bw.png");
        return image;
    }

    /**
     * Rotates the image 90 degrees clockwise.
     * @param image image to transform
     * @return image rotated 90 degrees clockwise
     */
    public  static Img RotateImage(Img image) throws IOException {
        int y = image.GetHeight();
        int x = image.GetWidth();
        Img rotated = new Img(y, x);
        int newY = 0;
        int newX = y-1;
        for(int r=0; r<x; r++) {
            newX = y-1;
            for (int c =0; c <y; c++) {
                RGB pixel = image.GetRGB(r, c);
                rotated.SetRGB(newX, newY, pixel);
                newX--;
            }
            newY++;
        }
        SaveImage(rotated, "/Users/dinatsang/Desktop/Music/rotated.png");
        return rotated;
    }

    /**
     * Applies an Instagram-like filter to the image. To do so, we apply the following transformations:
     * 1) We apply a "warm" filter. We can produce warm colors by reducing the amount of blue in the image
     *      and increasing the amount of red. For each pixel, apply the following transformation:
     *          r = r * 1.2
     *          g = g
     *          b = b / 1.5
     * 2) We add a vignette (a black gradient around the border) by combining our image with an
     *      an image of a halo (you can see the image at resources/halo.png). We take 65% of our
     *      image and 35% of the halo image. For example:
     *          r = .65 * r_image + .35 * r_halo
     * 3) We add decorative grain by combining our image with a decorative grain image
     *      (resources/decorative_grain.png). We will do this at a .95 / .5 ratio.
     * @param image image to transform
     * @return image with a filter
     * @throws IOException
     */
    public static Img InstagramFilter(Img image) throws IOException {
        int y = image.GetHeight();
        int x = image.GetWidth();
        for(int c=0; c<y; c++) {
            for (int r = 0; r < x; r++) {
                RGB pixel = image.GetRGB(r, c);
                int red = (int)(1.2 * pixel.GetRed());
                int green = pixel.GetGreen();
                int blue = (int)(pixel.GetBlue()/1.5);
                RGB changed = new RGB(red, green, blue);
                image.SetRGB(r, c, changed);
            }
        }
        Img halo = new Img("resources/halo.png");
        int haloX = 0;
        int haloY = 0;
        int avgY=halo.getHeight()/y;
        int avgTotalY=avgY;
        int avgX=halo.getWidth()/x;
        int avgTotalX=avgX;
        for(int c=0; c<y; c++) {
            if(c>avgTotalY){
                avgTotalY+=avgY;
                if(avgY>halo.getHeight()){
                    haloY=halo.getHeight()-1;
                }
                else{
                    haloY++;
                }
            }
            for (int r = 0; r < x; r++) {
                if(r>avgTotalX){
                    avgTotalX+=avgX;
                    if(avgX>halo.getWidth()){
                        haloX=halo.getWidth()-1;
                    }
                    else{
                        haloX++;
                    }
                }
                RGB pixel = image.GetRGB(r, c);
                RGB haloPixel = halo.GetRGB(haloX, haloY);
                int red = (int)((.65 * pixel.GetRed()) + (.35 * haloPixel.GetRed()));
                int green = (int)((.65 * pixel.GetGreen()) + (.35 * haloPixel.GetGreen()));
                int blue = (int)((.65 * pixel.GetBlue()) + (.35 * haloPixel.GetBlue()));
                RGB changed = new RGB(red, green, blue);
                image.SetRGB(r, c, changed);
            }
        }
        Img grain = new Img("resources/decorative_grain.png");
        int grainX = 0;
        int grainY = 0;
        int gAvgY=grain.getHeight()/y;
        int gAvgTotalY=gAvgY;
        int gAvgX=grain.getWidth()/x;
        int gAvgTotalX=gAvgX;
        for(int c=0; c<y; c++) {
            if(c>gAvgTotalY){
                gAvgTotalY+=gAvgY;
                grainY++;
            }
            for (int r = 0; r < x; r++) {
                if(r>gAvgTotalX){
                    gAvgTotalX+=gAvgX;
                    grainX++;
                }
                RGB pixel = image.GetRGB(r, c);
                RGB grainPixel = grain.GetRGB(haloX, haloY);
                int red = (int)((.95 * pixel.GetRed()) + (.05 * grainPixel.GetRed()));
                int green = (int)((.95 * pixel.GetGreen()) + (.05 * grainPixel.GetGreen()));
                int blue = (int)((.95 * pixel.GetBlue()) + (.05 * grainPixel.GetBlue()));
                RGB changed = new RGB(red, green, blue);
                image.SetRGB(r, c, changed);
            }
        }

        return image;
    }

    /**
     * Sets the given hue to each pixel image. Hue can range from 0 to 360. We do this
     * by converting each RGB pixel to an HSL pixel, Setting the new hue, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param hue amount of hue to add
     * @return image with added hue
     */
    public static Img SetHue(Img image, int hue) {
        int y = image.GetHeight();
        int x = image.GetWidth();
        for(int c=0; c<y; c++){
            for(int r=0; r<x; r++){
                RGB pixel = image.GetRGB(r, c);
                HSL newpixel = pixel.ConvertToHSL();
                int ogHue = newpixel.GetHue()+hue;
                newpixel.SetHue(ogHue);
                RGB change = newpixel.GetRGB();
                image.SetRGB(r, c, change);
            }
        }
        return image;
    }

    /**
     * Sets the given saturation to the image. Saturation can range from 0 to 1. We do this
     * by converting each RGB pixel to an HSL pixel, setting the new saturation, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param saturation amount of saturation to add
     * @return image with added hue
     */
    public static Img SetSaturation(Img image, double saturation) {
        int y = image.GetHeight();
        int x = image.GetWidth();
        for(int c=0; c<y; c++){
            for(int r=0; r<x; r++){
                RGB pixel = image.GetRGB(r, c);
                HSL newpixel = pixel.ConvertToHSL();
                double ogSat = newpixel.GetSaturation()+saturation;
                newpixel.SetSaturation(ogSat);
                RGB change = newpixel.GetRGB();
                image.SetRGB(r, c, change);
            }
        }
        return image;
    }

    /**
     * Sets the lightness to the image. Lightness can range from 0 to 1. We do this
     * by converting each RGB pixel to an HSL pixel, setting the new lightness, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param lightness amount of hue to add
     * @return image with added hue
     */
    public static Img SetLightness(Img image, double lightness) {
        int y = image.GetHeight();
        int x = image.GetWidth();
        for(int c=0; c<y; c++){
            for(int r=0; r<x; r++){
                RGB pixel = image.GetRGB(r, c);
                HSL newpixel = pixel.ConvertToHSL();
                double ogLight = newpixel.GetLightness()+lightness;
                newpixel.SetLightness(ogLight);
                RGB change = newpixel.GetRGB();
                image.SetRGB(r, c, change);
            }
        }
        return image;
    }
}
