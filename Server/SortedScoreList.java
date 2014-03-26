package Quiz;
import java.util.List;
/**
* A list which always contains ordered scores.
*/
public class SortedScoreList<Score> implements LinkedList<Score> {
	private LinkedList<Score> list;

	@Override
	public boolean add(Score, newScore){
		boolean inserted = false;

		if(list == null){
			list = new LinkedList<Score>();
			list.add(newScore);
			inserted = true;
		} else {
			for (int i = 0; i < list.length; i++) {
				Score nextScore = list.get(i);
				int comparison = newScore.compareTo(nextScore);
				if (comparison <= 0){
					list.add(i, newScore);
					i = list.length + 1;
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
}