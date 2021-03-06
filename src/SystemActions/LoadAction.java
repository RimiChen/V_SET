package SystemActions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFileChooser;

import CharaMake.CustomCharacter;
import CharacterScreen.G_Chara;
import EdittingBuffer.EditingBuffer;
import Events.EventPageFrame;
import Events.G_Event;
import Events.StoryDialogue;
import Events.StoryEvent;
import FunctionActions.AddCharaAction;
import FunctionActions.CharaButtonAction;
import MainScreen.CharacterButton;
import Variables.GlobalV;
import Variables.LookUp;
import Variables.SystemControl;

public class LoadAction implements ActionListener{
	final JFileChooser fc = new JFileChooser(".\\");
	public List<String> pathCollection;
	public String defaultPath;
	
	public LoadAction(){
		//fc.setCurrentDirectory(System.getProperty(".\\"));
		pathCollection = new ArrayList<String>();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		System.out.println("System: "+"Load ");
	    //Handle open button action.
	    //Handle open button action.
		if(GlobalV.CharaNumber==0){
			if(SystemControl.loadingMode == 0){
				//basic loading function
				loadFunction();
			}
			else{
				loadPaths();
				try {
					System.out.println("Load Characters: "+defaultPath+pathCollection.get(0));
					loadCharacter(defaultPath+pathCollection.get(0));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(int pathIter = 1; pathIter <pathCollection.size(); pathIter++){
					
					try {
						GlobalV.CurrentEdittingPage = pathIter;
						loadPages(defaultPath+pathCollection.get(pathIter), pathIter);
					} catch (NumberFormatException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
				GlobalV.CurrentEdittingPage = 1;

				
			}

		}
		else{
			System.out.println("System: you are current editting a story.");
		}
	}
	public void loadFunction(){
	    int returnVal = fc.showOpenDialog(null);
	    File file = fc.getSelectedFile();
	    
	    String filePath = "";
	    
	    
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	System.out.println("System: open file " + file.getPath());
	    	filePath = file.getPath();
	    } else { 
	    	System.out.println("System: cancel open ");
	    }

        FileReader fr = null;

       if(filePath != ""){
	        try {
				fr = new FileReader(file.getPath());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if(fr != null){
	        BufferedReader br = new BufferedReader(fr);
	        try {
				while (br.ready()) {
				    //process data here
					//System.out.println(br.readLine());
				    String tempString = br.readLine();
					String[] tempStringArray;
					
					tempStringArray = tempString.split(",");
					
					if(tempStringArray[0].equals("CNUM")){
						System.out.println("System: Load "+ Integer.parseInt(tempStringArray[1].replaceAll("\\s*", ""))+" character(s).");
						// create characters
						int numberCharacter = Integer.parseInt(tempStringArray[1].replaceAll("\\s*", ""));
						for(int i = 0; i< numberCharacter; i++){
							// Add new character
							String tempCharaInfo = br.readLine();
							String temp2 = tempCharaInfo.replaceAll("\\s*", "");
							String[] tempCharaInfoArray = temp2.split(",");
							
							GlobalV.CharaNumber = GlobalV.CharaNumber+1;
							//initialize
							CustomCharacter tempChara;
							CharacterButton tempCharaButton;
							CharaButtonAction tempCharaButtonAct;							
							
							tempChara = new CustomCharacter(GlobalV.CharaNumber, G_Chara.Man.initCharaImageSet);
							tempChara.name = tempCharaInfoArray[2].replaceAll("\\|"," ");
							tempChara.type = tempCharaInfoArray[3].replaceAll("\\s*","").replaceAll("\\|", " ");
							System.out.println("character type:" +tempChara.type);
							for(int k = 0; k<13; k++){
								tempChara.imageIndex.set(k, Integer.parseInt(tempCharaInfoArray[4+k].replaceAll("\\s*","")));
							}
							
							LookUp.CharaMap.put(GlobalV.CharaNumber, tempChara);
							LookUp.CharaNameMap.put(tempChara.name, GlobalV.CharaNumber);
							// create Character button
							tempCharaButton = new CharacterButton(
									G_Chara.charaButtonPanel.getWidth()*(tempChara.index-1)/ GlobalV.CharaNumber,
									0 ,
									G_Chara.charaButtonPanel.getWidth()/ GlobalV.CharaNumber,
									G_Chara.charaButtonPanel.getHeight()* 90/100,
									tempChara.index
									);
							tempCharaButton.setVisible(true);
							tempCharaButton.setText(tempChara.name);
							tempCharaButtonAct = new CharaButtonAction();
							tempCharaButton.addActionListener(tempCharaButtonAct);
							G_Chara.CharacterButtonMap.put(tempChara.name, tempCharaButton);
							
							AddCharaAction.updateButtonSize();
							G_Chara.charaButtonPanel.addToMap(tempCharaButton.getDepth(),G_Chara.CharacterButtonMap.get(tempChara.name));
							G_Chara.charaButtonPanel.removeAll();
							G_Chara.charaButtonPanel.addThings();
							G_Chara.charaButtonPanel.repaint();
						}
					}
					if(tempStringArray[0].equals("ENUM")){
						//load events
						int numberEvent = Integer.parseInt(tempStringArray[1].replaceAll("\\s*", ""));
						System.out.println("System: Load "+ numberEvent+" event(s).");
						
						int i = 0;
						while(i < numberEvent){
							String tempEventInfo = br.readLine();
							String temp2 = tempEventInfo.replaceAll("\\s*", "");
							String[] tempEventInfoArray = temp2.split(",");								
							if(tempEventInfoArray[0].equals("E")){
								i++;
								System.out.println("System: add event " + tempEventInfoArray[1].replaceAll("\\s*", ""));
						    	//Create a new event
								int tempX = Integer.parseInt(tempEventInfoArray[5].replaceAll("\\s*", ""));
								int tempY = Integer.parseInt(tempEventInfoArray[6].replaceAll("\\s*", ""));
								int tempSizeX = Integer.parseInt(tempEventInfoArray[7].replaceAll("\\s*", ""));
								int tempSizeY = Integer.parseInt(tempEventInfoArray[8].replaceAll("\\s*", ""));
								String tempEventName = tempEventInfoArray[3].replaceAll("\\s*", "").replaceAll("\\|"," ");
								String tempEventPlace = tempEventInfoArray[4].replaceAll("\\s*", "").replaceAll("\\|"," ");
						      	G_Event.createNewEvent(tempX, tempY, tempSizeX, tempSizeY, tempEventName, tempEventPlace, Integer.parseInt(tempEventInfoArray[1]));
						      	
						      	
						    	//GlobalV.isEditting = false;
						    	G_Event.region.updateStartPos(0, 0);
						      	G_Event.region.updateSize(0, 0);

						      	G_Event.DragPanel.removeAll();
						      	 
						       	G_Event.DragPanel.map.remove(G_Event.region.getDepth());
						      	G_Event.DragPanel.addThings();
						      	G_Event.DragPanel.repaint();
						      	G_Event.StorylinePanel.removeAll();
						      	G_Event.StorylinePanel.map.remove(G_Event.DragPanel.getDepth());
						      	//C_Event.StorylinePanel.
						      	G_Event.StorylinePanel.addThings();
						      	G_Event.StorylinePanel.repaint();
						      	
						      	//add dialogue
						      	int tempEventID = Integer.parseInt(tempEventInfoArray[1].replaceAll("\\s*", ""));
						      	int numberDialogue = Integer.parseInt(tempEventInfoArray[2].replaceAll("\\s*", ""));
						      	
						      	EditingBuffer.BufferedDialogue.clear();
						      	for(int j = 0; j < numberDialogue; j++){
						      		System.out.println("-Sub: Add dialogue "+j +" to event "+ i);
						      		String tempDia = br.readLine();
						      		String tempDia2 = tempDia.replaceAll("\\s*", "");
						      		String[] tempDiaArray = tempDia2.split(",");
					      			StoryEvent nowEvent = null;
					      			StoryDialogue tempDialogue;

						      		if(tempDiaArray[0].equals("EC")){
						      			// add dialogue and content

						      			nowEvent = LookUp.EventMap.get(tempEventID);
						      			if(EditingBuffer.BufferedDialogue.size() == 0){
						      				tempDialogue = new StoryDialogue(0, 0, G_Event.DialoguePanel.getWidth(), G_Event.DialoguePanel.getHeight()/GlobalV.MaxDialogueNumber, EditingBuffer.BufferedDialogue.size(),EditingBuffer.BufferedDialogue.size());
						      				EditingBuffer.BufferedDialogue.add(tempDialogue);
						      			}
						      			else{
						      				int current = EditingBuffer.BufferedDialogue.size();
						      				tempDialogue = new StoryDialogue(
						      						0,
						      						EditingBuffer.BufferedDialogue.get(current-1).getY()+EditingBuffer.BufferedDialogue.get(current-1).getHeight()+5,
						      						G_Event.DialoguePanel.getWidth(),
						      						G_Event.DialoguePanel.getHeight()/GlobalV.MaxDialogueNumber,
						      						EditingBuffer.BufferedDialogue.size(),
						      						EditingBuffer.BufferedDialogue.size()
						      				);
						      				EditingBuffer.BufferedDialogue.add(tempDialogue);
						      			}
						      			System.out.println("System: Add new dialogue in "+ nowEvent.eventName);							      			
						      		}
						      		EditingBuffer.BufferedDialogue.get(j).charaIndex = LookUp.CharaNameMap.get(tempDiaArray[2].replaceAll("\\s*", "").replaceAll("\\|", " "));
						      		EditingBuffer.BufferedDialogue.get(j).content = tempDiaArray[3].replaceAll("\\s*", "").replaceAll("\\|", " ");
						    		
						    		//nowEvent.dialogueQueue.clear();
						    		//for(int i = 0; i < GlobalV.BufferedDialogue.size(); i++){
						      		EditingBuffer.BufferedDialogue.get(j).charaIndex = LookUp.CharaNameMap.get(tempDiaArray[2].replaceAll("\\s*", "").replaceAll("\\|", " "));
						    		//}
						    		nowEvent.dialogueQueue.add(EditingBuffer.BufferedDialogue.get(j));
						      	}
						      	
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }		
	}
	public void loadCharacter(String path) throws IOException{
		
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        while (br.ready()) {
           // System.out.println(br.readLine());
		    String tempString = br.readLine();
			String[] tempStringArray;
			
			tempStringArray = tempString.split(",");
			
			if(tempStringArray[0].equals("CNUM")){
				System.out.println("System: Load "+ Integer.parseInt(tempStringArray[1].replaceAll("\\s*", ""))+" character(s).");
				// create characters
				int numberCharacter = Integer.parseInt(tempStringArray[1].replaceAll("\\s*", ""));
				for(int i = 0; i< numberCharacter; i++){
					// Add new character
					String tempCharaInfo = br.readLine();
					String temp2 = tempCharaInfo.replaceAll("\\s*", "");
					String[] tempCharaInfoArray = temp2.split(",");
					
					GlobalV.CharaNumber = GlobalV.CharaNumber+1;
					//initialize
					CustomCharacter tempChara;
					CharacterButton tempCharaButton;
					CharaButtonAction tempCharaButtonAct;							
					
					tempChara = new CustomCharacter(GlobalV.CharaNumber, G_Chara.Man.initCharaImageSet);
					tempChara.name = tempCharaInfoArray[2].replaceAll("\\|"," ");
					tempChara.type = tempCharaInfoArray[3].replaceAll("\\s*","").replaceAll("\\|", " ");
					System.out.println("character type:" +tempChara.type);
					for(int k = 0; k<13; k++){
						tempChara.imageIndex.set(k, Integer.parseInt(tempCharaInfoArray[4+k].replaceAll("\\s*","")));
					}
					
					LookUp.CharaMap.put(GlobalV.CharaNumber, tempChara);
					LookUp.CharaNameMap.put(tempChara.name, GlobalV.CharaNumber);
					// create Character button
					tempCharaButton = new CharacterButton(
							G_Chara.charaButtonPanel.getWidth()*(tempChara.index-1)/ GlobalV.CharaNumber,
							0 ,
							G_Chara.charaButtonPanel.getWidth()/ GlobalV.CharaNumber,
							G_Chara.charaButtonPanel.getHeight()* 90/100,
							tempChara.index
							);
					tempCharaButton.setVisible(true);
					tempCharaButton.setText(tempChara.name);
					tempCharaButtonAct = new CharaButtonAction();
					tempCharaButton.addActionListener(tempCharaButtonAct);
					G_Chara.CharacterButtonMap.put(tempChara.name, tempCharaButton);
					
					AddCharaAction.updateButtonSize();
					G_Chara.charaButtonPanel.addToMap(tempCharaButton.getDepth(),G_Chara.CharacterButtonMap.get(tempChara.name));
					G_Chara.charaButtonPanel.removeAll();
					G_Chara.charaButtonPanel.addThings();
					G_Chara.charaButtonPanel.repaint();
				}
			}
           
        }
        fr.close();
		
	}
	public void loadPages(String path, int pageNumber) throws NumberFormatException, IOException{
		if(GlobalV.NumberEventPage <pageNumber){
			for(int i = GlobalV.NumberEventPage; i<pageNumber; i++){
				GlobalV.NumberEventPage++;
				System.out.println("System: create a new Story Page "+GlobalV.NumberEventPage);
				EventPageFrame tempEventPage = new EventPageFrame(0, 0, G_Event.StorylinePanel.getWidth(), G_Event.StorylinePanel.getHeight(), GlobalV.NumberEventPage, GlobalV.NumberEventPage);
				G_Event.EventPageMap.put(GlobalV.NumberEventPage, tempEventPage);
				Map< String, Integer> tempPosition = new TreeMap< String, Integer>();
				LookUp.EventPositionMap.add(tempPosition);
				Map< Integer, String> tempTime = new TreeMap<Integer, String>();
				LookUp.EventTimeMap.add(tempTime);
			}
		}
		System.out.println("\r\n=========================================================");

		FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        while (br.ready()) {
           // System.out.println(br.readLine());
		    String tempString = br.readLine();
			String[] tempStringArray;
			
			tempStringArray = tempString.split(",");
			
			if(tempStringArray[0].equals("ENUM")){
				//load events
				int numberEvent = Integer.parseInt(tempStringArray[1].replaceAll("\\s*", ""));
				System.out.println("System: Load "+ numberEvent+" event(s).");
				
				int i = 0;
				while(i < numberEvent){
					String tempEventInfo = br.readLine();
					String temp2 = tempEventInfo.replaceAll("\\s*", "");
					String[] tempEventInfoArray = temp2.split(",");								
					if(tempEventInfoArray[0].equals("E")){
						i++;
						System.out.println("System: add event " + tempEventInfoArray[1].replaceAll("\\s*", ""));
				    	//Create a new event
						int tempX = Integer.parseInt(tempEventInfoArray[5].replaceAll("\\s*", ""));
						int tempY = Integer.parseInt(tempEventInfoArray[6].replaceAll("\\s*", ""));
						int tempSizeX = Integer.parseInt(tempEventInfoArray[7].replaceAll("\\s*", ""));
						int tempSizeY = Integer.parseInt(tempEventInfoArray[8].replaceAll("\\s*", ""));
						String tempEventName = tempEventInfoArray[3].replaceAll("\\s*", "").replaceAll("\\|"," ");
						String tempEventPlace = tempEventInfoArray[4].replaceAll("\\s*", "").replaceAll("\\|"," ");
				      	G_Event.createNewEvent(tempX, tempY, tempSizeX, tempSizeY, tempEventName, tempEventPlace, Integer.parseInt( tempEventInfoArray[1]));
				      	

				      	if(pageNumber ==1){
					    	//GlobalV.isEditting = false;
					    	G_Event.region.updateStartPos(0, 0);
					      	G_Event.region.updateSize(0, 0);
	
					      	G_Event.DragPanel.removeAll();
					      	 
					       	G_Event.DragPanel.map.remove(G_Event.region.getDepth());
					      	G_Event.DragPanel.addThings();
					      	G_Event.DragPanel.repaint();
					      	G_Event.StorylinePanel.removeAll();
					      	G_Event.StorylinePanel.map.remove(G_Event.DragPanel.getDepth());
					      	//C_Event.StorylinePanel.
					      	G_Event.StorylinePanel.addThings();
					      	G_Event.StorylinePanel.repaint();
					      	G_Event.paintEventButton();
				      	}
				      	
				      	//add dialogue
				      	int tempEventID = Integer.parseInt(tempEventInfoArray[1].replaceAll("\\s*", ""));
				      	int numberDialogue = Integer.parseInt(tempEventInfoArray[2].replaceAll("\\s*", ""));
				      	
				      	EditingBuffer.BufferedDialogue.clear();
				      	for(int j = 0; j < numberDialogue; j++){
				      		System.out.println("-Sub: Add dialogue "+j +" to event "+ tempEventInfoArray[1]);
				      		String tempDia = br.readLine();
				      		String tempDia2 = tempDia.replaceAll("\\s*", "");
				      		String[] tempDiaArray = tempDia2.split(",");
			      			StoryEvent nowEvent = null;
			      			StoryDialogue tempDialogue;

				      		if(tempDiaArray[0].equals("EC")){
				      			// add dialogue and content

				      			nowEvent = LookUp.EventMap.get(tempEventID);
				      			if(EditingBuffer.BufferedDialogue.size() == 0){
				      				tempDialogue = new StoryDialogue(0, 0, G_Event.DialoguePanel.getWidth(), G_Event.DialoguePanel.getHeight()/GlobalV.MaxDialogueNumber, EditingBuffer.BufferedDialogue.size(),EditingBuffer.BufferedDialogue.size());
				      				EditingBuffer.BufferedDialogue.add(tempDialogue);
				      			}
				      			else{
				      				int current = EditingBuffer.BufferedDialogue.size();
				      				tempDialogue = new StoryDialogue(
				      						0,
				      						EditingBuffer.BufferedDialogue.get(current-1).getY()+EditingBuffer.BufferedDialogue.get(current-1).getHeight()+5,
				      						G_Event.DialoguePanel.getWidth(),
				      						G_Event.DialoguePanel.getHeight()/GlobalV.MaxDialogueNumber,
				      						EditingBuffer.BufferedDialogue.size(),
				      						EditingBuffer.BufferedDialogue.size()
				      				);
				      				EditingBuffer.BufferedDialogue.add(tempDialogue);
				      			}
				      			System.out.println("System: Add new dialogue in "+ nowEvent.eventName);							      			
				      		}
				      		EditingBuffer.BufferedDialogue.get(j).charaIndex = LookUp.CharaNameMap.get(tempDiaArray[2].replaceAll("\\s*", "").replaceAll("\\|", " "));
				      		EditingBuffer.BufferedDialogue.get(j).content = tempDiaArray[3].replaceAll("\\s*", "").replaceAll("\\|", " ");
				    		
				    		//nowEvent.dialogueQueue.clear();
				    		//for(int i = 0; i < GlobalV.BufferedDialogue.size(); i++){
				      		EditingBuffer.BufferedDialogue.get(j).charaIndex = LookUp.CharaNameMap.get(tempDiaArray[2].replaceAll("\\s*", "").replaceAll("\\|", " "));
				    		//}
				    		nowEvent.dialogueQueue.add(EditingBuffer.BufferedDialogue.get(j));
				      	}
				      	
					}
				}
			}
           
        }
        fr.close();		
	}
	
	public String getDefaultPath(String currentFilePath){
		String result= "";
		int pathIndex = -1;
		
		pathIndex = currentFilePath.lastIndexOf('\\');
		if(pathIndex <0){
			System.out.println("Default path: null");
		}
		else{
			System.out.println("Default path: "+currentFilePath.substring(0, pathIndex+1));
			result = currentFilePath.substring(0, pathIndex+1);
		}
		return result;
	}
	public void loadPaths(){
	    int returnVal = fc.showOpenDialog(null);
	    File file = fc.getSelectedFile();
	    
	    String filePath = "";
	    
	    
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	System.out.println("System: open file " + file.getPath());
	    	filePath = file.getPath();
	    	defaultPath =  getDefaultPath(filePath);
	    } else { 
	    	System.out.println("System: cancel open ");
	    }

        FileReader fr = null;

       if(filePath != ""){
	        try {
				fr = new FileReader(file.getPath());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if(fr != null){
	        BufferedReader br = new BufferedReader(fr);
	        try {
				while (br.ready()) {
				    //process data here
					String tempString = br.readLine();
					//System.out.println(tempString);
				    
					pathCollection.add(tempString);
					
					//first file is character
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }		
		
	}
		

}
