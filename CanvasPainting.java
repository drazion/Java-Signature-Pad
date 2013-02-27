package signature;
/**
 * Developed by Aaron Harvey 
 * aharvey@ecsgrid.com
 * Build Date: April 20, 2012
 * Copyright 2012 Aaron Harvey

   Copyright (c) 2013 Aaron Harvey

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class CanvasPainting extends Applet implements MouseMotionListener, MouseListener, ActionListener {

	//Variables
	int width = 400, height = 55;
	private ArrayList<Point> points = new ArrayList<Point>();
	HttpURLConnection httpUrlConnection;  
   
	//Image save
	BufferedImage backbuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	Graphics backg = backbuffer.getGraphics();
    Upload upload = new Upload();
    
	//Buttons
	Button saveButton;
	Button clearButton;
   
	public void init() {
	  setSize(width, height+50);
	  backg.setColor( Color.white );
      backg.fillRect( 0, 0, width+1, height );
      backg.setColor( Color.black );
      setBackground(Color.black);
     
      addMouseMotionListener( this );
      addMouseListener(this);
     
      setLayout(null);
      saveButton = new Button("Save");
      clearButton = new Button("Clear");
      saveButton.setBounds(10, height+10, 100, 30);
      clearButton.setBounds(120, height+10, 100, 30);
      add(saveButton);
      add(clearButton);
      clearButton.addActionListener(this);
      saveButton.addActionListener(this);
	}

   /**
    * Draw the line when user drags mouse
    */
   public void mouseDragged( MouseEvent e ) {
      points.add(e.getPoint());
      Graphics2D g2 = (Graphics2D)backg;
      g2.setStroke(new BasicStroke(2));
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      for (int i = 0; i < points.size() - 2; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
      }
      repaint();
      e.consume();
   }
  
   /**
    * Action Listener for the Applet Buttons
    */
   public void actionPerformed(ActionEvent evt) {
		   //Save Image
		   if(evt.getSource() == saveButton) {
			   try {
				   //Move buttons off screen
				   saveButton.setBounds(-100, -100, 100, 30);
				   clearButton.setBounds(-100, -100, 100, 30);
				   
				   //Anti Alias
				   Graphics2D g2 = (Graphics2D)backg;
				   g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				   repaint();   
				   //Blur Signature
				   float[] matrix = {
					        0.111f, 0.111f, 0.111f, 
					        0.111f, 0.111f, 0.111f, 
					        0.111f, 0.111f, 0.111f, 
					    };
				   BufferedImageOp op = new ConvolveOp(new Kernel(3,3, matrix), ConvolveOp.EDGE_NO_OP,null );
				   backbuffer = op.filter(backbuffer, null);
				   repaint();
				   //Convert image information to Array
				   ByteArrayOutputStream baos = new ByteArrayOutputStream();
				   ImageIO.write(backbuffer, "png", baos);
				   baos.flush();
				   byte[] imageInByte = baos.toByteArray();
				   
				   //Pass Image to be uploaded
				   upload.httpConn(imageInByte);
				   baos.close();
				   
				   //Clear signature and thank customer
				   points.clear();
				  
			//Catch Error
			} catch (IOException e) {
					e.printStackTrace();	
			}
	 } 
	  
	   //Clear the drawing Area
	   else if (evt.getSource() == clearButton) {
		   saveButton.setBounds(10, height+10, 100, 30);
		   backg.setColor(Color.white);
		   backg.fillRect(0,0,width,height);
		   points.clear();
		   repaint(); 
		   //Set Color back to black for drawing
		   backg.setColor(Color.black);  
	   }
   }
   
   
   public void mouseReleased(MouseEvent e) {
       points.clear();
   }

   public void update( Graphics g ) {
      g.drawImage(backbuffer,0,0, this);
   }

   public void paint( Graphics g ) {
      update( g );
   }

   /**
    * Do nothing with these methods
    */
	@Override
	public void mouseClicked(MouseEvent e) { }
	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) { }
	@Override
	public void mousePressed(MouseEvent e) { }
	@Override
	public void mouseMoved(MouseEvent e) { }
}

