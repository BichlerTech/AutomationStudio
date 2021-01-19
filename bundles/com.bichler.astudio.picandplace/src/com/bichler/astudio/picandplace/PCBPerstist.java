package com.bichler.astudio.picandplace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class PCBPerstist {

	private int columnX = -1;
	private int columnY = -1;
	private int columnAngle = -1;
	private PCBBoards boards = new PCBBoards();
	private PCBBoard board = new PCBBoard();

//	public static void main(String args[]) {
//		PCBPerstist pers = new PCBPerstist();
//		pers.loadBoard();
//		PCBBoard board = pers.getBoard();
//		board.setWidth(120.0);
//		board.setHeight(45.0);
//		// 90 degree
//		board.rotateBoard90();
//		// 180 degree
//		board.rotateBoard90();
//		// 270 degree
//		board.rotateBoard90();
//		
//		//board.mirrorVertical();
//		
//		PCBBoards boards = new PCBBoards();
//		boards.setOriginal(board);
//		
//		boards.generatePanel(1, 4, 2, 0);
//		return;
//	}

	public PCBPerstist() {
		
	}
	
	public PCBBoards getBoards() {
		return boards;
	}
	
	public PCBBoard getBoard() {
		return board;
	}
	
	public void loadBoard() {
		File file1 = new File("C:\\Users\\hbich\\Desktop\\PaP.txt");
		File file = new File("C:\\Users\\Thomas\\Desktop\\PaP.txt");
		PickAndPlaceElement element = new PickAndPlaceElement();
		Map<Integer, String> attributes = new HashMap<Integer, String>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			boolean firstline = true;
			while ((line = reader.readLine()) != null) {

				if (line.startsWith("#")) {
					continue;
				}
				
				line = line.replaceAll(" {2,}", " ");
				System.out.println(line);
				String columns[] = line.split(" ");

				if(!firstline) {
					element = new PickAndPlaceElement();
					board.getElements().add(element);
				}
				for (int i = 0; i < columns.length; i++) {
					if (firstline) {
						if (columns[i].compareTo("Center-X(mm)") == 0) {
							columnX = i;
						} else if (columns[i].compareTo("Center-Y(mm)") == 0) {
							columnY = i;
						} else if (columns[i].compareTo("Rotation") == 0) {
							columnAngle = i;
						} else {
							attributes.put(i, columns[i]);
						}
					} else {
						
						if (i == columnAngle) {
							element.setAngle(Double.parseDouble(columns[i]));
						} else if (i == columnX) {
							element.setX(Double.parseDouble(columns[i]));
						} else if (i == columnY) {
							element.setY(Double.parseDouble(columns[i]));
						} else {
							element.getAttributes().put(attributes.get(i), columns[i]);
						}
					}
				}
				firstline = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
