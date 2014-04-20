import java.util.AbstractSequentialList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.io.Serializable;

/**
* A list which orders scores as they are added.
*
* Scores are added from the end, so scores with the same points are in chronological
* order.
*/
public class SortedScoreList extends AbstractSequentialList<Score> implements Serializable {
	private List<Score> list;

	public SortedScoreList(){
		this.list = new LinkedList<Score>();		
	}

	public List<Score> getList(){
		return this.list;
	}

	public void setList(List<Score> newList){
		this.list = newList;
	}

	public boolean add(Score newScore){
		boolean inserted = false;

		if (list.size() == 0){
			list.add(newScore);
			inserted = true;
		} else {
			for (int i = list.size() - 1; i >= 0; i--) {
				Score prevScore = list.get(i);
				int comparison = newScore.compareTo(prevScore);
				if (comparison <= 0){
					list.add(i+1, newScore);
					i = -1;
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