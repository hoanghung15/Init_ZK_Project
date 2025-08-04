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
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

import java.util.Date;
import java.util.List;

public class AssignTaskController extends SelectorComposer<Component> {

    @Wire
    public Combobox cbStaffInfo, cbTypeContract, cbTypeTask, cbTypeDepartment, cbStatusTask, cbCreatorTask;

    @Wire
    public Datebox startDateTask, endDateTask, createdDateTask;

    @Wire
    public Button btnSaveTaskForm;

    @Wire
    public Textbox txtDescriptionTask;

    private final StaffDAO staffDAO = new StaffDAO();
    private final ContractDAO contractDAO = new ContractDAO();
    private final AssignTaskDAO assignTaskDAO = new AssignTaskDAO();
    private final CookieService cookieService = new CookieService();
    private final JwtServiceImpl jwtService = new JwtServiceImpl();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Selectors.wireComponents(comp, this, false);

        applyComboBoxStaff();
        applyComboboxContractName();
        applyCreatedBy();

        btnSaveTaskForm.addEventListener(Events.ON_CLICK, e -> createTaskForm());
    }

    /** Load combobox tên hợp đồng */
    public void applyComboboxContractName() {
        List<Contract> contractList = contractDAO.getAllContractWithFilter(null, null, null, null, null);
        cbTypeContract.getItems().clear();
        for (Contract contract : contractList) {
            Comboitem comboitem = new Comboitem(contract.getName());
            comboitem.setValue(contract.getId());
            comboitem.setParent(cbTypeContract);
        }
    }

    /** Set người tạo mặc định */
    private void applyCreatedBy() {
        User user = getUserLogin();
        System.out.println("apply user: " + user);

        cbCreatorTask.getItems().clear();

        if (user != null) {
            Comboitem comboitem = new Comboitem(user.getUsername());
            comboitem.setValue(user.getId());
            comboitem.setParent(cbCreatorTask);

            // Chọn luôn item này để hiển thị
            cbCreatorTask.setSelectedItem(comboitem);
        }
    }

    /** Load combobox danh sách nhân viên */
    public void applyComboBoxStaff() {
        List<Staff> staffList = staffDAO.getAllStaff();
        cbStaffInfo.getItems().clear();
        for (Staff staff : staffList) {
            Comboitem comboitem = new Comboitem(staff.getName());
            comboitem.setValue(staff.getId());
            comboitem.setParent(cbStaffInfo);
        }
    }

    /** Lưu task mới */
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

    /** Lấy thông tin user đang login */
    public User getUserLogin() {
        String token = cookieService.getTokenFromCookie();
        if (token == null) return null;

        String username = jwtService.extractUsername(token);
        if (username == null) return null;

        UserDAO userDAO = new UserDAO();
        return userDAO.getUser(username);
    }
}
