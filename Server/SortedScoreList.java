import java.util.AbstractSequentialList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
* A list which orders scores as they are added.
*/
public class SortedScoreList extends AbstractSequentialList {
	List<Score> list;

	public SortedScoreList(){
		list = new LinkedList<Score>();		
	}

	public boolean add(Score newScore){
		boolean inserted = false;

		if (list.size() == 0){
			list.add(newScore);
			inserted = true;
		} else {
			for (int i = 0; i < list.size(); i++) {
				Score nextScore = list.get(i);
				int comparison = newScore.compareTo(nextScore);
				if (comparison <= 0){
					list.add(i, newScore);
					i = list.size() + 1;
					inserted = true;
				}
			}

			if (!inserted){
				list.add(newScore);
				inserted = true;
			}
		}
		return inserted;
	}

	@Override
	public ListIterator listIterator(int i){
		ListIterator li = list.listIterator();
		return li;
	}

	public int size(){
		return list.size();
	}

	public void print(){
		for (Score s : list) {
			System.out.print(s.getPoints());
			System.out.print(", ");
		}
	}
}