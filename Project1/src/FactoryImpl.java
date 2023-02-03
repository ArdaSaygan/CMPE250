import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 * FactoryImpl
 */
public class FactoryImpl implements Factory {

    // Necesary Fields
    private Holder first;
    private Holder last;
    private Integer size;

    public FactoryImpl() {
        size = 0;
    }

    @Override
    public void addFirst(Product product) {
        // TODO Auto-generated method stub
        
        switch (size) {
            // no products case
            case 0:
                Holder holder0 = new Holder(null, product, null);
                first = holder0;
                last = holder0;
                break;

            // single or multiple products case
            default:
                Holder holder1 = new Holder(null, product, first);
                first.setPreviousHolder(holder1);
                first = holder1;
                break;
        }

        // increment size by 1
        size++;
        
    }

    @Override
    public void addLast(Product product) {
        // TODO Auto-generated method stub
        switch (size) {
            // no products case
            case 0:
                Holder holder0 = new Holder(null, product, null);
                first = holder0;
                last = holder0;
                break;

            // single or multiple products case
            default:
                Holder holder1 = new Holder(last, product, null);
                last.setNextHolder(holder1);
                last = holder1;
                break;
        }

        // increment size by 1
        size++;
    }

    @Override
    public Product removeFirst() throws NoSuchElementException {
        // TODO Auto-generated method stub

        switch (size) {
            // no elements
            case 0: 
                throw new NoSuchElementException();
            
            // single element
            case 1:
                Product firstProduct1 = first.getProduct();
                first = null;
                last = null;
                size--;
                return firstProduct1;

            // multiple elements
            default:
                Product firstProductm = first.getProduct();
                Holder secondHolder = first.getNextHolder();
                first = secondHolder;
                secondHolder.setPreviousHolder(null);
                size--;
                return firstProductm;
        }

    }

    @Override
    public Product removeLast() throws NoSuchElementException {
        // TODO Auto-generated method stub
        switch (size) {
            // no elements
            case 0: 
                throw new NoSuchElementException();
            
            // single element
            case 1:
                Product lasProduct1 = last.getProduct();
                first = null;
                last = null;
                size--;
                return lasProduct1;

            // multiple elements
            default:
                Product lasProductm = last.getProduct();
                Holder secfromlast = last.getPreviousHolder();
                last = secfromlast;
                secfromlast.setNextHolder(null);
                size--;
                return lasProductm;
        }
    }

    @Override
    public Product find(int id) throws NoSuchElementException {
        // TODO Auto-generated method stub
        Holder curr = first;

        // if factory is empty
        if (curr == null){ throw new NoSuchElementException(); }
            
        while (curr != null) {
            if (curr.getProduct().getId() == id){
                return curr.getProduct();
            }
            curr = curr.getNextHolder();
        }

        throw new NoSuchElementException();
    }

    @Override
    public Product update(int id, Integer value) throws NoSuchElementException {
        // TODO Auto-generated method stub

        Holder curr = first;

        // if factory is empty
        if (curr == null){ throw new NoSuchElementException(); }
            
        while (curr != null) {
            if (curr.getProduct().getId() == id){
                Product preProduct = new Product(curr.getProduct().getId(), curr.getProduct().getValue());
                curr.getProduct().setValue(value);
                return preProduct;
                // THERE MAY BE A PROBLEM HERE, CHECK PLEASE
            }
            curr = curr.getNextHolder();
        }

        throw new NoSuchElementException();
    }

    @Override
    public Product get(int index) throws IndexOutOfBoundsException {
        // TODO Auto-generated method stub

        Holder curr = first;

        // if factory is empty
        if (curr == null){ throw new IndexOutOfBoundsException();}

        while (curr != null && index > 0){
            curr = curr.getNextHolder();
            index--;
        }

        if (curr == null){
            throw new IndexOutOfBoundsException();
        }

        return curr.getProduct();

    }

    // should i think of the case that an element it inserted on the index == size? YES IMPLEMENT THAT
    @Override
    public void add(int index, Product product) throws IndexOutOfBoundsException {
        // TODO Auto-generated method stub

        Holder curr = first;

        // if factory is empty
        if (size == 0 && index != 0){ throw new IndexOutOfBoundsException();}

        // special cases
        if (index == 0){ addFirst(product); return;}
        if (index == size){ addLast(product); return;}

        // else
        while (curr != null && index > 0){
            curr = curr.getNextHolder();
            index--;
        }

        if (curr == null){
            throw new IndexOutOfBoundsException();
        }

        Holder precurr = curr.getPreviousHolder();

        // create a new holder for product
        Holder newHolder = new Holder(precurr, product, curr);
        precurr.setNextHolder(newHolder);
        curr.setPreviousHolder(newHolder);

        size++;
    }

    @Override
    public Product removeIndex(int index) throws IndexOutOfBoundsException {
        // TODO Auto-generated method stub
        Holder curr = first;

        // if factory is empty
        if (size == 0){ throw new IndexOutOfBoundsException();}

        // if first element is removed
        if (index == 0){ 
            Product product = first.getProduct();
            removeFirst(); 
            return product;
        }

        // if last element is removed
        if (index == size - 1){
            Product product = last.getProduct();
            removeLast();
            return product;
        }

        // else
        while (curr != null && index > 0){
            curr = curr.getNextHolder();
            index--;
        }

        if (curr == null){
            throw new IndexOutOfBoundsException();
        }

        Holder precurr = curr.getPreviousHolder();
        Holder nextcurr = curr.getNextHolder();

        // create a new holder for product
        Product product = curr.getProduct();
        precurr.setNextHolder(nextcurr);
        nextcurr.setPreviousHolder(precurr);
        size--;

        return product;
    }

    @Override
    public Product removeProduct(int value) throws NoSuchElementException {
        // TODO Auto-generated method stub
        Holder curr = first;

        while (curr != null) {
            if (curr.getProduct().getValue() == value) {
                // remove product and return
                Holder precurr = curr.getPreviousHolder();
                Holder nextcurr = curr.getNextHolder();

                if (precurr == null){ removeFirst(); return curr.getProduct();}
                if (nextcurr == null){ removeLast(); return curr.getProduct();}

                precurr.setNextHolder(nextcurr);
                nextcurr.setPreviousHolder(precurr);
                size--;

                return curr.getProduct();
            }

            curr = curr.getNextHolder();
        }

        throw new NoSuchElementException();
    }

    @Override
    public int filterDuplicates() {
        // TODO Auto-generated method stub
        Holder curr = first;
        HashSet<Integer> seenvalues = new HashSet<Integer>();
        int noremovedProducts = 0;

        while (curr != null) {
            Integer value = curr.getProduct().getValue();

            if (seenvalues.contains(value)){
                // remove and incerement noremovedProducts
                Holder precurr = curr.getPreviousHolder();
                Holder nextcurr = curr.getNextHolder();

                if (precurr == null) {
                    removeFirst();
                    noremovedProducts++;
                    curr = nextcurr;
                    continue;}
                if (nextcurr == null) { 
                    removeLast(); 
                    noremovedProducts++;
                    curr = nextcurr;
                    continue;
                }

                precurr.setNextHolder(nextcurr);
                nextcurr.setPreviousHolder(precurr);
                size--;
                noremovedProducts++;
                curr = nextcurr;
                continue;
            }

            seenvalues.add(value);
            curr = curr.getNextHolder();
        }

        return noremovedProducts;
    }

    @Override
    public void reverse() {
        // TODO Auto-generated method stub

        if (size == 0 || size == 1) { return;}

        Holder curr = last;
        while (curr != null) {
            Holder precurr = curr.getPreviousHolder();
            Holder nextcurr = curr.getNextHolder();

            curr.setNextHolder(precurr);
            curr.setPreviousHolder(nextcurr);

            curr = curr.getNextHolder();
        }

        Holder temp = last;
        last = first;
        first = temp;

    }


    @Override
    public String toString(){
        Holder curr = first;
        if (curr == null){
            return "{}";
        }

        String result = "{" + curr.getProduct().toString();
        curr = curr.getNextHolder();

        while (curr != null) {
            result = result + "," + curr.getProduct().toString();
            curr = curr.getNextHolder();
        }
        result = result + "}";
        return result;
    }
    
}