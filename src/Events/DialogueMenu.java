package Events;

import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import FunctionActions.GivanMenuAction;

@SuppressWarnings("serial")
public class DialogueMenu extends GivenMenu{
	int locationX;
	int locationY;
	int sizeX;
	int sizeY;
	
	int depth;
	
	String menuName;
	int dialogueID;
	
	
	GivanMenuAction act;

	@SuppressWarnings("rawtypes")
	DefaultListModel defaultListModel = new DefaultListModel();  
	
	public  DialogueMenu(int LocationX, int LocationY, int SizeX, int SizeY, int Depth, String MenuName, int DialogueID, Map<String, Integer> Map){
		super();
		
		this.locationX = LocationX;
		this.locationY = LocationY;
		this.sizeX = SizeX;
		this.sizeY = SizeY;
		this.depth = Depth;
		
		this.dialogueID = DialogueID;
		
		setLayout(null);
		setLocation(locationX, locationY);
		setSize(sizeX, sizeY);
		setVisible(true);
				
		this.menuName = MenuName;
		
		//act = new GivanMenuAction(this);
		
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setLayoutOrientation(JList.VERTICAL);
		setVisibleRowCount(-1);
		
		
		updateMenu(menuName, Map);
  		
		
		
	}
	public void setAction(GivanMenuAction act){
		this.act = act;
		addListSelectionListener(act);
	}
	@SuppressWarnings("unchecked")
	public void updateMenu(String MenuName, Map<String, Integer> NewMap){
		defaultListModel.clear();
		for(String key : NewMap.keySet())
		{
			defaultListModel.addElement(key);  
		}
        setModel(defaultListModel);
	}
	public void setDepth(int Depth){
		this.depth = Depth;
	}
	public int getDepth(){
		return depth;
	}
}
