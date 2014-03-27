import java.util.AbstractSequentialList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
* A list which always contains ordered scores.
*/
public class SortedScoreList extends AbstractSequentialList {
	List<Score> list;

	public boolean add(Score newScore){
		boolean inserted = false;

		if(list == null){
			list = new LinkedList<Score>();
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

	public ListIterator listIterator(int i){
		ListIterator li = list.listIterator();

		return li;
	}

	public int size(){
		return list.size();
	}

	public void print(){
		for (Object o : list) {
			Score s = (Score) o;
			System.out.print(s.getPoints());
			System.out.print(", ");
		}
	}

	public static void main(String[] args) {
		SortedScoreList test = new SortedScoreList(); 
		Score a = new Score(100);
		Score b = new Score(1);
		Score c = new Score(56);
		test.add(a);
		test.add(b);
		test.add(c);
		test.print();
	}
}