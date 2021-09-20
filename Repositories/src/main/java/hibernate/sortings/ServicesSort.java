package hibernate.sortings;

public enum ServicesSort implements ISortType {

    ASC_SORT_BY_INDEX( 1 ),
    DESC_SORT_BY_INDEX( 2 ),

    ASC_SORT_BY_PRICE( 3),
    DESC_SORT_BY_PRICE( 4);


    private final Integer sortIndex;

    ServicesSort( Integer sortIndex){

        this.sortIndex= sortIndex;
    }



    public Integer getSortIndex() {
        return sortIndex;
    }

}
