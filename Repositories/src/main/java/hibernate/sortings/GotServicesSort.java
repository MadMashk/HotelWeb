package hibernate.sortings;

public enum GotServicesSort implements ISortType{
    ASC_SORT_BY_PRICE( 1),
    DESC_SORT_BY_PRICE( 2),
    ASC_SORT_BY_DATE( 3),
    DESC_SORT_BY_DATE( 4);


    private final Integer sortIndex;

    GotServicesSort( Integer sortIndex){
        this.sortIndex= sortIndex;
    }



    public Integer getSortIndex() {
        return sortIndex;
    }
}
