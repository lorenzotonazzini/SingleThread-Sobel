package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import panel.ImagePanel;

public class Main {

	public static void main(String[] args) throws IOException {
		
		JFrame frame = new JFrame("Border Detector");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLocationByPlatform(true);

		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select an image");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "png", "gif", "bmp");
		jfc.addChoosableFileFilter(filter);
		
		int returnValue = jfc.showOpenDialog(null);

		File imgFile = null;
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			imgFile = jfc.getSelectedFile();
		}
		else {
			return;
		}
		
		long startTime = System.currentTimeMillis();
		
		BufferedImage image = ImageIO.read(imgFile);
		
		int[][] gray = new int[image.getWidth()][image.getHeight()];
		
		int rgb;
		for(int y =0; y<image.getHeight(); ++y) {
			for(int x=0; x<image.getWidth(); ++x) {
				rgb = image.getRGB(x, y);
				gray[x][y] = (int)(0.2126 * ((rgb >> 16) & 0xff) + 0.7152 * ((rgb >> 8) & 0xff) + 0.0722 * ((rgb) & 0xff));
			}
		}
		
        int x = image.getWidth();
        int y = image.getHeight();

        int[][] edgeColors = new int[x][y];
        int maxGradient = -1;

        int gx, gy, g;
        
        for (int i = 1; i < x - 1; ++i) {
            for (int j = 1; j < y - 1; ++j) {

                gx =  ((-1 * gray[i - 1][j - 1]) + (0 * gray[i - 1][j]) + (1 * gray[i - 1][j + 1])) 
                        + ((-2 * gray[i][j - 1]) + (0 * gray[i][j]) + (2 * gray[i][j + 1]))
                        + ((-1 * gray[i + 1][j - 1]) + (0 * gray[i + 1][j]) + (1 * gray[i + 1][j + 1]));

                gy =  ((-1 * gray[i - 1][j - 1]) + (-2 * gray[i - 1][j]) + (-1 * gray[i - 1][j + 1]))
                        + ((0 * gray[i][j - 1]) + (0 * gray[i][j]) + (0 * gray[i][j + 1]))
                        + ((1 * gray[i + 1][j - 1]) + (2 * gray[i + 1][j]) + (1 * gray[i + 1][j + 1]));

                g = (int) Math.sqrt((gx * gx) + (gy * gy));

                if(maxGradient < g) {
                    maxGradient = g;
                }

                edgeColors[i][j] = g;
            }
        }

        double scale = 255.0 / maxGradient;
        int edgeColor;
        
        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                edgeColor = (int) (edgeColors[i][j] * scale);
                image.setRGB(i, j, 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor);
            }
        }

        long estimatedTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Elapsed Time: " + estimatedTime + "ms");
        
        ImagePanel background = new ImagePanel(image);
		
		frame.add(background);
		
		frame.setSize(x+15, y+35);
		
		frame.setVisible(true);
	}

}
