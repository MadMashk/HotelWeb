package hibernate.sortings;

import hibernate.*;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SortingNavigator {

    private final  List<ISortType> clientSortList = new ArrayList<>(Arrays.stream(ClientSort.values()).collect(Collectors.toList()));
    private final  List<ISortType> roomSortList = new ArrayList<>(Arrays.stream(RoomSort.values()).collect(Collectors.toList()));
    private final  List<ISortType> serviceSortList = new ArrayList<>(Arrays.stream(ServicesSort.values()).collect(Collectors.toList()));
    private final  List<ISortType> gotServicesSortList = new ArrayList<>(Arrays.stream(GotServicesSort.values()).collect(Collectors.toList()));
    private final  List<ISortType> rentSortList = new ArrayList<>(Arrays.stream(RentSort.values()).collect(Collectors.toList()));

    @Autowired
    private ClientDao clientDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private ServiceDao serviceDao;
    @Autowired
    private RentDao rentDao;
    @Autowired
    private GotServicesDao gotServicesDao;

    public List<Client> clientSort(Integer sortIndex) {
        for (ISortType clientSort : clientSortList) {
            if (clientSort.getSortIndex().equals(sortIndex)) {
                return clientDao.getAll(clientSort);
            }
        }
        return new ArrayList<>();
    }

    public List<Room> roomSort(Integer sortIndex) {
        for (ISortType roomSort : roomSortList) {
            if (roomSort.getSortIndex().equals(sortIndex)) {
                return roomDao.getAll(roomSort);
            }
        }
        return new ArrayList<>();
    }

    public List<Service> serviceSort(Integer sortIndex) {
        for (ISortType serviceSort : serviceSortList) {
            if (serviceSort.getSortIndex().equals(sortIndex)) {
                return serviceDao.getAll(serviceSort);
            }
        }
        return new ArrayList<>();
    }

    public List<Rent> rentSort(Integer sortIndex) {
        for (ISortType rentSort : rentSortList) {
            if (rentSort.getSortIndex().equals(sortIndex)) {
                return rentDao.getAll(rentSort);
            }
        }
        return new ArrayList<>();
    }

    public List<GotServices> gotServicesSort(Integer sortIndex) {
        for (ISortType gotServicesSort : gotServicesSortList) {
            if (gotServicesSort.getSortIndex().equals(sortIndex)) {
                return gotServicesDao.getAll(gotServicesSort);
            }
        }
        return new ArrayList<>();
    }
}
