package headfirst.designpatterns.proxy.virtualproxy;

import java.net.*;
import java.util.SplittableRandom;
import java.awt.*;
import javax.swing.*;

class ImageProxy implements Icon {
	volatile ImageIcon imageIcon;
	final URL imageURL;
	Thread retrievalThread;
	boolean retrieving = false;

	SplittableRandom random;

	public ImageProxy(URL url) {
		imageURL = url;
		random = new SplittableRandom();
	}
     
	public int getIconWidth() {
		if (imageIcon != null) {
            return imageIcon.getIconWidth();
        } else {
			return 800;
		}
	}
 
	public int getIconHeight() {
		if (imageIcon != null) {
            return imageIcon.getIconHeight();
        } else {
			return 600;
		}
	}
	
	synchronized void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}
     
	public void paintIcon(final Component c, Graphics  g, int x,  int y) {
		if (imageIcon != null) {
			imageIcon.paintIcon(c, g, x, y);
		} else {
			g.drawString("Loading album cover, please wait...", x+300, y+190);
			if (!retrieving) {
				retrieving = true;
				
				// retrievalThread = new Thread(new Runnable() {
				// 	public void run() {
				// 		try {
				// 			setImageIcon(new ImageIcon(imageURL, "Album Cover"));
				// 			c.repaint();
				// 		} catch (Exception e) {
				// 			e.printStackTrace();
				// 		}
				// 	}
				// });
				
				retrievalThread = new Thread(() -> {
						try {
							int rd = random.nextInt(1000, 5000);
							System.out.println("Sleeping for " + rd + " ms ...");
							Thread.sleep(rd);
							setImageIcon(new ImageIcon(imageURL, "Album Cover"));
							c.repaint();
						} catch (Exception e) {
							e.printStackTrace();
						}
				});
				retrievalThread.start();
				
			}
		}
	}
}
