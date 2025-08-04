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
            cbStatusTask, cbCreatorTask, cbAssignee;

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

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Selectors.wireComponents(comp, this, false);

        // Load initial data
        staffList = staffDAO.getAllStaff();
        applyComboBoxStaff();
        applyAssigneeTask();
        applyComboboxContractName();
        applyCreatedBy();
        applyListTask();

        // Button lưu
        btnSaveTaskForm.addEventListener(Events.ON_CLICK, event -> {
            createTaskForm();
            Messagebox.show("Tạo mới công việc");
            Executions.sendRedirect(null);
        });

        // Event thay đổi phòng ban
        cbTypeDepartment.addEventListener(Events.ON_CHANGE, event -> {
            Comboitem selectedItem = cbTypeDepartment.getSelectedItem();
            if (selectedItem != null) {
                String selectedDepartment = selectedItem.getValue();
                System.out.println("Selected department: " + selectedDepartment);

                staffList = staffDAO.getAllStaffByDepartment(selectedDepartment);

                applyComboBoxStaff();
            }
        });
    }

    /**
     * Load combobox tên hợp đồng
     */
    public void applyComboboxContractName() {
        List<Contract> taskList = contractDAO.getAllContractWithFilter(null, null, null, null, null);
        cbTypeContract.getItems().clear();

        for (Contract task : taskList) {
            Comboitem comboitem = new Comboitem(task.getName());
            comboitem.setValue(task.getId());
            comboitem.setParent(cbTypeContract);
        }
    }

    /**
     * Set người tạo mặc định
     */
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

    /**
     * Load combobox nhân viên chính
     */
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

    /**
     * Load combobox nhân viên nhận task (Assignee)
     */
    private void applyAssigneeTask() {
        User user = getUserLogin();
        System.out.println("apply user: " + user);

        cbAssignee.getItems().clear();

        if (user != null) {
            Comboitem comboitem = new Comboitem(user.getUsername());
            comboitem.setValue(user.getId());
            comboitem.setParent(cbAssignee);

            // Chọn luôn item này để hiển thị
            cbAssignee.setSelectedItem(comboitem);
        }
    }

    /**
     * Lưu task mới
     */
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

    /**
     * Lấy thông tin user đang login
     */
    public User getUserLogin() {
        String token = cookieService.getTokenFromCookie();
        if (token == null) return null;

        String username = jwtService.extractUsername(token);
        if (username == null) return null;

        UserDAO userDAO = new UserDAO();
        return userDAO.getUser(username);
    }

    /**
     * Hiển thị danh sách task trong listbox
     */
    public void applyListTask() {
        List<Task> taskList = assignTaskDAO.getAllAssignTask();
        ListModelList<Task> model = new ListModelList<>(taskList);
        taskListBox.setModel(model);

        taskListBox.setItemRenderer((Listitem item, Task task, int index) -> {
            item.setValue(task);
            Contract contract = contractDAO.getContractById(task.getContract_id());

            new Listcell(String.valueOf(index + 1)).setParent(item);
            new Listcell(task.getType() != null ? task.getType() : "").setParent(item);
            new Listcell(contract != null ? contract.getNumberContract() : "").setParent(item);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            new Listcell(task.getStartDate() != null ? sdf.format(task.getStartDate()) : "").setParent(item);
            new Listcell(task.getEndDate() != null ? sdf.format(task.getEndDate()) : "").setParent(item);

            new Listcell(task.getStatus() != null ? task.getStatus() : "").setParent(item);
            new Listcell(getUserLogin() != null ? getUserLogin().getUsername() : "").setParent(item);
        });
    }
}
