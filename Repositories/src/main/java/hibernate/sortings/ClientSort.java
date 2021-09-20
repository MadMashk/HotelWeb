package hibernate.sortings;

public enum ClientSort implements ISortType{
    ASC_BY_NAME( 1),
    DESC_BY_NAME( 2);

    private final Integer sortIndex;

    ClientSort( Integer sortIndex){

        this.sortIndex= sortIndex;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }
}
