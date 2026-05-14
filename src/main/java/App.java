import config.DatabaseConfig;
import controller.EmployeeController;
import mapper.EmployeeMapper;
import repository.EmployeeRepository;
import service.EmployeeService;
import service.impl.EmployeeServiceImpl;
import view.EmployeeView;

void main() {

    DatabaseConfig.initConnection();

    EmployeeView view = new EmployeeView();
    EmployeeMapper mapper = new EmployeeMapper();
    EmployeeRepository repository = new EmployeeRepository();

    EmployeeService service = new EmployeeServiceImpl(repository, mapper);

    EmployeeController controller = new EmployeeController(view, service);

    controller.start();
}