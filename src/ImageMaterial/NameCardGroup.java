package ImageMaterial;

import java.util.ArrayList;
import java.util.List;

import CharaMake.ImageGroup;

public class NameCardGroup implements ImageGroup{

	@Override
	public List<PathNameNumber> getImagePathList() {
		// TODO Auto-generated method stub
		PathNameNumber temp;
		
		List<PathNameNumber> tempList;
		tempList = new ArrayList<PathNameNumber>();
		//0
		temp = new PathNameNumber(".\\DialogueMaterials\\", "NameCard", 1);
		tempList.add(temp);

		return tempList;
	}

}
