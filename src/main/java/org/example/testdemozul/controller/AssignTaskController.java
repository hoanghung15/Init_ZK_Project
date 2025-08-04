package org.example.testdemozul.controller;

import org.example.testdemozul.dao.AssignTaskDAO;
import org.example.testdemozul.dao.ContractDAO;
import org.example.testdemozul.dao.StaffDAO;
import org.example.testdemozul.dao.UserDAO;
import org.example.testdemozul.model.Contract;
import org.example.testdemozul.model.Staff;
import org.example.testdemozul.model.Task;
import org.example.testdemozul.model.User;
import org.example.testdemozul.service.CookieService;
import org.example.testdemozul.service.JwtServiceImpl;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.text.SimpleDateFormat;
import java.util.List;

public class AssignTaskController extends SelectorComposer<Component> {

    // UI components
    @Wire
    private Listbox taskListBox;

    @Wire
    public Combobox cbStaffInfo, cbTypeContract, cbTypeTask, cbTypeDepartment,
            cbStatusTask, cbCreatorTask, cbAssignee, cbFilterStaff, cbIdTaskUnassign;

    @Wire
    public Datebox startDateTask, endDateTask, createdDateTask;

    @Wire
    public Button btnSaveTaskForm;

    @Wire
    public Textbox txtDescriptionTask;

    // DAO & Service
    private final StaffDAO staffDAO = new StaffDAO();
    private final ContractDAO contractDAO = new ContractDAO();
    private final AssignTaskDAO assignTaskDAO = new AssignTaskDAO();
    private final CookieService cookieService = new CookieService();
    private final JwtServiceImpl jwtService = new JwtServiceImpl();

    // Data
    private List<Staff> staffList;
    private List<Task> taskList;
    private List<Task> unAssignedTaskList;

    private Integer tmpTaskId = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Selectors.wireComponents(comp, this, false);

        // Load initial data
        staffList = staffDAO.getAllStaff();
        taskList = assignTaskDAO.getAllAssignTaskWithFilter(null);
        unAssignedTaskList = assignTaskDAO.getAllAssignTaskWithFilter(-1);

        applyComboBoxStaff();
        applyAssigneeTask();
        applyComboboxContractName();
        applyCreatedBy();
        applyUnassignTask();

        // renderListTask
        applyListTask();

        // Button lưu
        btnSaveTaskForm.addEventListener(Events.ON_CLICK, event -> {
            if (tmpTaskId == null) {
                createTaskForm();
                Messagebox.show("Tạo mới công việc");
                Executions.sendRedirect(null);
            } else {
                updateTaskForm();
                Messagebox.show("Update");
            }

        });

        // Event thay đổi phòng ban
        cbTypeDepartment.addEventListener(Events.ON_CHANGE, event -> {
            Comboitem selectedItem = cbTypeDepartment.getSelectedItem();
            if (selectedItem != null) {
                String selectedDepartment = selectedItem.getValue();
                staffList = staffDAO.getAllStaffByDepartment(selectedDepartment);

                applyComboBoxStaff();
            }
        });

        cbFilterStaff.addEventListener(Events.ON_CHANGE, event -> {
            Comboitem selected = cbFilterStaff.getSelectedItem();
            String value = selected != null ? selected.getValue() : "all";

            switch (value) {
                case "assigned":
                    taskList = assignTaskDAO.getAllAssignTaskWithFilter(1);     // staff_id > 0
                    break;
                case "unassigned":
                    taskList = assignTaskDAO.getAllAssignTaskWithFilter(-1);    // staff_id = 0
                    break;
                default:
                    taskList = assignTaskDAO.getAllAssignTaskWithFilter(null);  // tất cả
                    break;
            }
            // Đổ dữ liệu lên listbox
            applyListTask();

        });

        taskListBox.addEventListener(Events.ON_SELECT, event -> {
            if (taskListBox.getSelectedItem() != null) {
                Task task = taskListBox.getSelectedItem().getValue();
                tmpTaskId = task.getId();
                fillForm(task);
            }

        });
    }

    /*** Load combobox tên hợp đồng*/
    public void applyComboboxContractName() {
        List<Contract> taskList = contractDAO.getAllContractWithFilter(null, null, null, null, null);
        cbTypeContract.getItems().clear();
        for (Contract task : taskList) {
            Comboitem comboitem = new Comboitem(task.getName());
            comboitem.setValue(task.getId());
            comboitem.setParent(cbTypeContract);
        }
    }

    /*** Set người tạo mặc định*/
    private void applyCreatedBy() {
        User user = getUserLogin();
        cbCreatorTask.getItems().clear();

        if (user != null) {
            Comboitem comboitem = new Comboitem(user.getUsername());
            comboitem.setValue(user.getId());
            comboitem.setParent(cbCreatorTask);

            // Chọn luôn item này để hiển thị
            cbCreatorTask.setSelectedItem(comboitem);
        }
    }

    /*** Load combobox nhân viên chính*/
    public void applyComboBoxStaff() {
        cbStaffInfo.getItems().clear();
        if (staffList == null || staffList.isEmpty()) return;

        for (Staff staff : staffList) {
            Comboitem comboitem = new Comboitem(staff.getName());
            comboitem.setValue(staff.getId());
            comboitem.setParent(cbStaffInfo);
        }
        cbStaffInfo.invalidate(); // refresh UI
    }

    /*** Load combobox nhân viên giao task (Assignee)*/
    private void applyAssigneeTask() {
        User user = getUserLogin();

        cbAssignee.getItems().clear();

        if (user != null) {
            Comboitem comboitem = new Comboitem(user.getUsername());
            comboitem.setValue(user.getId());
            comboitem.setParent(cbAssignee);

            // Chọn luôn item này để hiển thị
            cbAssignee.setSelectedItem(comboitem);
        }
    }


    /*** Load comboBox task chưa được giao (unassign) */
    private void applyUnassignTask() {
        cbIdTaskUnassign.getItems().clear();
        for (Task task : unAssignedTaskList) {
            Comboitem comboitem = new Comboitem(String.valueOf(task.getId()));
            comboitem.setValue(task.getId());
            comboitem.setParent(cbIdTaskUnassign);
        }
    }

    /*** Lưu task mới*/
    public void createTaskForm() {
        Task task = new Task();
        task.setType(cbTypeTask.getValue());
        task.setDepartment(cbTypeDepartment.getValue());
        task.setStartDate(startDateTask.getValue());
        task.setEndDate(endDateTask.getValue());
        task.setDescription(txtDescriptionTask.getValue());
        task.setStatus(cbStatusTask.getValue());
        task.setUpdateAt(createdDateTask.getValue()); // java.util.Date

        // Lấy giá trị từ combobox
        if (cbStaffInfo.getSelectedItem() != null) {
            task.setStaff_id(cbStaffInfo.getSelectedItem().getValue());
        }
        if (cbTypeContract.getSelectedItem() != null) {
            task.setContract_id(cbTypeContract.getSelectedItem().getValue());
        }
        if (cbCreatorTask.getSelectedItem() != null) {
            task.setCreatBy_id(cbCreatorTask.getSelectedItem().getValue());
        }

        assignTaskDAO.creatNewAssignTask(task);
    }

    public void updateTaskForm() {
        Task task = new Task();
        task.setId(tmpTaskId);
        task.setType(cbTypeTask.getValue());
        task.setDepartment(cbTypeDepartment.getValue());
        task.setStartDate(startDateTask.getValue());
        task.setEndDate(endDateTask.getValue());
        task.setDescription(txtDescriptionTask.getValue());
        task.setStatus(cbStatusTask.getValue());
        task.setUpdateAt(createdDateTask.getValue()); // java.util.Date

        // Lấy giá trị từ combobox
        if (cbStaffInfo.getSelectedItem() != null) {
            task.setStaff_id(cbStaffInfo.getSelectedItem().getValue());
        }
        if (cbTypeContract.getSelectedItem() != null) {
            task.setContract_id(cbTypeContract.getSelectedItem().getValue());
        }
        if (cbCreatorTask.getSelectedItem() != null) {
            task.setCreatBy_id(cbCreatorTask.getSelectedItem().getValue());
        }

        System.out.println("Test " + task.toString());
        assignTaskDAO.updateAssignTask(task);
    }

    /*** Lấy thông tin user đang login*/
    public User getUserLogin() {
        String token = cookieService.getTokenFromCookie();
        if (token == null) return null;

        String username = jwtService.extractUsername(token);
        if (username == null) return null;

        UserDAO userDAO = new UserDAO();
        return userDAO.getUser(username);
    }

    /*** Hiển thị danh sách task trong listbox*/
    public void applyListTask() {

        ListModelList<Task> model = new ListModelList<>(taskList);
        taskListBox.setModel(model);

        taskListBox.setItemRenderer((Listitem item, Task task, int index) -> {
            item.setValue(task);
            Contract contract = contractDAO.getContractById(task.getContract_id());

            new Listcell(String.valueOf(index + 1)).setParent(item);
            new Listcell(String.valueOf(task.getId())).setParent(item);
            new Listcell(task.getType() != null ? task.getType() : "").setParent(item);
            new Listcell(contract != null ? contract.getNumberContract() : "").setParent(item);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            new Listcell(task.getStartDate() != null ? sdf.format(task.getStartDate()) : "").setParent(item);
            new Listcell(task.getEndDate() != null ? sdf.format(task.getEndDate()) : "").setParent(item);

            new Listcell(task.getStatus() != null ? task.getStatus() : "").setParent(item);

        });
    }

    public void fillForm(Task task) {
        clearForm();

        Staff staff = staffDAO.getStaffById(task.getStaff_id());
        Contract contract = contractDAO.getContractById(task.getContract_id());

        // Set giá trị cho các field
        cbTypeTask.setValue(task.getType());
        cbTypeDepartment.setValue(staff.getDepartment());
        txtDescriptionTask.setValue(task.getDescription());
        startDateTask.setValue(task.getStartDate());
        endDateTask.setValue(task.getEndDate());
        cbStatusTask.setValue(task.getStatus());
        createdDateTask.setValue(task.getStartDate());

        // Chọn nhân viên và hợp đồng đã có trong combobox
        selectComboboxByValue(cbStaffInfo, staff.getId());
        selectComboboxByValue(cbTypeContract, contract.getId());
    }


    public void clearForm() {
        // Xóa các Textbox và Datebox
        txtDescriptionTask.setValue("");
        startDateTask.setValue(null);
        endDateTask.setValue(null);
        cbStatusTask.setValue("");
        createdDateTask.setValue(null);
    }
    private void selectComboboxByValue(Combobox combobox, Object value) {
        for (Comboitem item : combobox.getItems()) {
            if (item.getValue().equals(value)) {
                combobox.setSelectedItem(item);
                return;
            }
        }
        // Nếu không tìm thấy, bỏ chọn
        combobox.setSelectedItem(null);
    }

}
