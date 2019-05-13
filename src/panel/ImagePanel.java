package panel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.JPanel;

public class ImagePanel extends JPanel implements Serializable {
	BufferedImage image = null;

	public ImagePanel(BufferedImage image) {
		this.image = image;
	}

	public void setImage(BufferedImage image){
		this.image = image;
	}

	public BufferedImage getImage(){
		return image;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); 
		Graphics2D g2 = (Graphics2D)g;
	
		if (image != null) {
			g2.drawImage(image, 0, 0, this);
		}
	}
}
