package Events;

import javax.swing.JButton;

import FunctionActions.EventConditionAddAction;


@SuppressWarnings("serial")
public class EventConditionAddButton extends JButton{
	int locationX;
	int locationY;
	int sizeX;
	int sizeY;
	
	int depth;
	int index;
	
	int page;
	//String name;
	
	EventConditionAddAction act;
	
	public EventConditionAddButton(int LocationX, int LocationY, int SizeX, int SizeY, int Depth, int page){
		this.locationX = LocationX;
		this.locationY = LocationY;
		this.sizeX = SizeX;
		this.sizeY = SizeY;
		this.depth = Depth;
		this.index = Depth;
		//this.name = name;
		
		
		this.page = page;
		
		act = new EventConditionAddAction();
		
		addActionListener(act);
		
		//setText(name);
		setLocation(locationX, locationY);
		setSize(sizeX, sizeY);		
	}
	public void setDepth(int Depth){
		this.depth = Depth;
	}
	public int getDepth(){
		return depth;
	}
/*	
	public void updateName(String name){
		this.name = name;
	}
*/
}