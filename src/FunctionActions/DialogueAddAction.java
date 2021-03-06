package FunctionActions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import EdittingBuffer.EditingBuffer;
import Events.G_Event;
import Events.StoryDialogue;
import Events.StoryEvent;

import Variables.GlobalV;
import Variables.LookUp;

public class DialogueAddAction  implements ActionListener{

	StoryEvent nowEvent;
	StoryDialogue tempDialogue;
	
	int numberInAPage;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		numberInAPage = GlobalV.MaxDialogueNumber;
		
		nowEvent = LookUp.EventMap.get(LookUp.EventNameMap.get(GlobalV.CurrentEditingEvent));
		//if(EditingBuffer.BufferedChoice.size()==0 && EditingBuffer.BufferedDialogue.size()==0){
			//G_Event.ContentOutPanel.remove(G_Event.ChoicePanel.getDepth());
			//G_Event.ContentOutPanel.map.remove(G_Event.ChoicePanel.getDepth());
		if(EditingBuffer.BufferedChoice.size()==0){	

			G_Event.ContentOutPanel.map.clear();
			G_Event.ContentOutPanel.addToMap(G_Event.DialoguePanel.getDepth(), G_Event.DialoguePanel);
			G_Event.ContentOutPanel.removeAll();
			G_Event.ContentOutPanel.addThings();
			G_Event.ContentOutPanel.repaint();
		//}
			
		//if(nowEvent.choiceQueue.size()==0){	
			if(EditingBuffer.BufferedDialogue.size() == 0){
				tempDialogue = new StoryDialogue(0, 0, G_Event.DialoguePanel.getWidth(), G_Event.DialoguePanel.getHeight()/numberInAPage, EditingBuffer.BufferedDialogue.size(),EditingBuffer.BufferedDialogue.size());
				EditingBuffer.BufferedDialogue.add(tempDialogue);
				int current = EditingBuffer.BufferedDialogue.size();
				G_Event.DialoguePanel.removeAll();
				G_Event.DialoguePanel.addToMap(EditingBuffer.BufferedDialogue.get(current-1).getDepth(), EditingBuffer.BufferedDialogue.get(current-1));
				G_Event.DialoguePanel.addThings();
				G_Event.DialoguePanel.repaint();
			}
			else{
				int current = EditingBuffer.BufferedDialogue.size();
				tempDialogue = new StoryDialogue(
						0,
						EditingBuffer.BufferedDialogue.get(current-1).getY()+EditingBuffer.BufferedDialogue.get(current-1).getHeight()+5,
						G_Event.DialoguePanel.getWidth(),
						G_Event.DialoguePanel.getHeight()/numberInAPage,
						EditingBuffer.BufferedDialogue.size(),
						EditingBuffer.BufferedDialogue.size()
				);
				EditingBuffer.BufferedDialogue.add(tempDialogue);
				current = EditingBuffer.BufferedDialogue.size();
				G_Event.DialoguePanel.removeAll();
				G_Event.DialoguePanel.addToMap(EditingBuffer.BufferedDialogue.get(current-1).getDepth(), EditingBuffer.BufferedDialogue.get(current-1));
				G_Event.DialoguePanel.addThings();
				G_Event.DialoguePanel.repaint();
			}
			System.out.println("System: Add new dialogue in "+ nowEvent.eventName);
		}
		else{
			System.out.println("System: Now this event work on choices, Please create new event to edit dialogues");
		}
	}

}
