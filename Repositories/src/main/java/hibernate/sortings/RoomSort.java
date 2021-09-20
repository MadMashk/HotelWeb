package hibernate.sortings;

public enum RoomSort implements ISortType{
    ASC_CAPACITY_SORT( 1),
    DESC_CAPACITY_SORT( 2),

    ASC_STARS_SORT( 3),
    DESC_STARS_SORT( 4),

    ASC_PRICE_SORT( 5),
    DESC_PRICE_SORT( 6);


    private final Integer sortIndex;

    RoomSort( Integer sortIndex){

        this.sortIndex= sortIndex;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

}
