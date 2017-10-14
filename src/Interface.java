import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class Interface extends JFrame{

	public final JTextArea TextEnter = new JTextArea();;

	public JLabel outImage = new JLabel();
	
	public JButton update = new JButton("Update Preview!"), save = new JButton("Save Image!");
	
	public BufferedImage boardImage;
	
	public BufferedImage unscailedImage;
	
	public ArrayList<String> words = new ArrayList<String>();
	
	public final char[] letters = {'\'','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	public final Color Text = new Color(255,255,255);
	public final Color TextBackground = new Color(64,64,64);
	public final Color Background = new Color(32,32,32);
	
	public final int imageSize = 800;
	
	public int tColor;
	public int fColor = new Color(0,0,0).getRGB();
	
	public String font = "Consolas";
	
	public Interface(){
		setTitle("Repetition Analyzer");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Background);
		
		boardImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
		outImage.setIcon(new ImageIcon(boardImage));
		outImage.setBounds(750, 50, imageSize, imageSize);
		add(outImage);
		
		TextEnter.setFont(new Font(font, Font.BOLD, 24));
		TextEnter.setBorder(BorderFactory.createLineBorder(Text));
		TextEnter.setForeground(Text);
		TextEnter.setBackground(TextBackground);
		TextEnter.setOpaque(true);
		JScrollPane sp = new JScrollPane(TextEnter); 
		sp.setBounds(50, 50, 650, 700);
		sp.setForeground(Text);
		sp.setBackground(TextBackground);
		add(sp);
		
		update.setBounds(50, 750, 650, 50);
		update.setFont(new Font(font, Font.BOLD, 30));
		update.setForeground(Text);
		update.setBackground(TextBackground);
		update.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   updateImage();
			   }
			});
		add(update);
		
		save.setBounds(50, 800, 650, 50);
		save.setFont(new Font(font, Font.BOLD, 30));
		save.setForeground(Text);
		save.setBackground(TextBackground);
		save.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   saveImg();
			   }
			});
		add(save);
		
		setSize(1600,940);
		setVisible(true);
	}
	
	public BufferedImage scaleImage(BufferedImage before, int nW, int nH){
		BufferedImage resized = new BufferedImage(nW, nH, before.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); 
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(before, 0, 0, nW, nH, 0, 0, before.getWidth(), before.getHeight(), null);
		g.dispose();
		return resized;
	}
	
	public BufferedImage scaleImageBy(BufferedImage before, int scale){
		BufferedImage resized = new BufferedImage(before.getWidth()*scale, before.getHeight()*scale, before.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); 
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(before, 0, 0, before.getWidth()*scale, before.getHeight()*scale, 0, 0, before.getWidth(), before.getHeight(), null);
		g.dispose();
		return resized;
	}
	
	public void updateWordList(){
		words = new ArrayList<String>();
		String textInput = TextEnter.getText();
		String tempWord;
		char tempChar;

		tempWord = "";
		for(int i = 0; i < textInput.length(); i++){
			tempChar = Character.toLowerCase(textInput.charAt(i));
			
			if(isLetter(tempChar)){
				tempWord += String.valueOf(tempChar);
			} else {
				if(!tempWord.equals("")){
					words.add(tempWord);
					tempWord = "";
				}
			}
		} if(!tempWord.equals("")){
			words.add(tempWord);
			tempWord = "";
		}
	}
	
	public void saveImg(){
		updateImage();
		try {
			String saveName = "";
			for(int i = 0; i < Math.min(words.size(),5); i++){
				if(i == Math.min(words.size(),5)-1){
					saveName += (words.get(i).substring(0, 1).toUpperCase() + words.get(i).substring(1));
				} else {
					saveName += ((words.get(i).substring(0, 1).toUpperCase() + words.get(i).substring(1)) + " ");
				}
			}
			File outputfile = new File("(" + saveName + "...).png");
			ImageIO.write(unscailedImage, "png", outputfile);
			
			Desktop.getDesktop().open(outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public long hash(String string) {
		  long h = 1125899906842597L; // prime
		  int len = string.length();

		  for (int i = 0; i < len; i++) {
		    h = 31*h + string.charAt(i);
		  }
		  return h;
		}
	
	public void updateImage(){
		Random rand = new Random();
		int temp;
		updateWordList();
		
		boardImage = new BufferedImage(words.size(), words.size(), BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < words.size(); x++){
			for(int y = 0; y < words.size(); y++){
				rand.setSeed(hash(words.get(x)));
				
				temp = rand.nextInt(3);
				if(temp == 0){
					tColor = new Color(rand.nextInt(128)+128, rand.nextInt(128), rand.nextInt(128)).getRGB();
				} else if (temp == 1){
					tColor = new Color(rand.nextInt(128), rand.nextInt(128)+128, rand.nextInt(128)).getRGB();
				} else if (temp == 2){
					tColor = new Color(rand.nextInt(128), rand.nextInt(128), rand.nextInt(128)+128).getRGB();
				} else {
					tColor = new Color(255,255,255).getRGB();
				}
				
				if(words.get(x).equals(words.get(y))){
					boardImage.setRGB(x, y, tColor);
				} else {
					boardImage.setRGB(x, y, fColor);
				}
			}
		}
		
		this.unscailedImage = scaleImageBy(boardImage , 4);
		boardImage = scaleImage(boardImage, imageSize, imageSize);
		
		outImage.setIcon(new ImageIcon(boardImage));
	}
	
	private boolean isLetter(char let){
		for(int i = 0; i < letters.length; i++){
			if(let == letters[i]){
				return true;
			}
		}
		return false;
	}
	
}
