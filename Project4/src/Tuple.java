public class Tuple<N, D> implements Comparable<Tuple> {
    N first;
    D second;

    public Tuple(N first, D second){
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Tuple o) {
        return Integer.compare((Integer) this.second, (Integer) o.second);
    }
}
