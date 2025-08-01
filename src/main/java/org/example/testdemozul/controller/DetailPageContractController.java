package org.example.testdemozul.controller;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import org.example.testdemozul.dao.ContractDAO;
import org.example.testdemozul.dao.StaffDAO;
import org.example.testdemozul.model.Contract;
import org.example.testdemozul.model.Staff;
import org.example.testdemozul.security.FirebaseConfig;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.text.SimpleDateFormat;
import java.util.List;

public class DetailPageContractController extends SelectorComposer<Component> {
    @Wire
    private Listbox contractListbox;

    @Wire
    private Textbox txtNumberContract, txtNameContract, txtEmailA, txtPhoneA, txtEmailB, txtPhoneB, txtApprover, searchBox;
    @Wire
    private Combobox cbStatus, cbScope, cbContractType, cbPaymentMethod, cbApprover, cbRoleFilter,cbScopeFilter, cbStatusFilter, cbTypeFilter;
    @Wire
    private Datebox dbStartDate, dbEndDate;
    @Wire
    private Fileupload fileContract;

    @Wire
    private Button btnRefreshForm, btnSaveForm, btnCancel;

    private ContractDAO contractDAO = new ContractDAO();
    private StaffDAO staffDAO = new StaffDAO();

    private List<Contract> contracts;

    private String fileURL = null;
    private String searchTmp = null;
    private String roleTmp = null;
    private String statusTmp = null;
    private String scopeTmp = null;
    private String typeTmp = null;

    private Integer currentIdContract;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        FirebaseConfig.initFirebase();

        // Load danh sách hợp đồng
        contracts = contractDAO.getAllContractWithFilter(null,null,null,null,null);
        ListModelList<Contract> model = new ListModelList<>(contracts);
        contractListbox.setModel(model);

        // Render danh sách
        contractListbox.setItemRenderer((Listitem item, Contract contract, int index) -> {
            item.setValue(contract);
            new Listcell(String.valueOf(index + 1)).setParent(item);
            new Listcell(String.valueOf(contract.getId())).setParent(item);
            new Listcell(contract.getNumberContract()).setParent(item);
            new Listcell(contract.getName()).setParent(item);

            // Trạng thái
            Listcell statusCell = new Listcell(contract.getStatus());
            if ("PENDING".equalsIgnoreCase(contract.getStatus())) {
                statusCell.setStyle("color: #d4a017;");
            } else if ("DONE".equalsIgnoreCase(contract.getStatus())) {
                statusCell.setStyle("color: #28a745;");
            } else {
                statusCell.setStyle("color: #6c757d;");
            }
            statusCell.setParent(item);

            // Link file
            Listcell fileCell = new Listcell();
            A viewLink = new A("Xem file hợp đồng");
            viewLink.setStyle("text-decoration: underline;");
            viewLink.setSclass("text-blue-500 underline cursor-pointer hover:underline");
            viewLink.addEventListener(Events.ON_CLICK, event -> {
                String filePath = contract.getFile_data();
                if (filePath != null && !filePath.isEmpty()) {
                    Executions.getCurrent().sendRedirect(filePath, "_blank");
                } else {
                    Messagebox.show("Không tìm thấy file đính kèm!", "Thông báo", Messagebox.OK, Messagebox.EXCLAMATION);
                }
            });
            viewLink.setParent(fileCell);
            fileCell.setParent(item);

            // Ngày bắt đầu và kết thúc
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            new Listcell(contract.getStartDate() != null ? sdf.format(contract.getStartDate()) : "").setParent(item);
            new Listcell(contract.getEndDate() != null ? sdf.format(contract.getEndDate()) : "").setParent(item);

            // Action buttons
            Listcell actionCell = new Listcell();
            Button btnEdit = new Button("Sửa");
            btnEdit.setSclass("bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded-lg me-2");
            btnEdit.addEventListener(Events.ON_CLICK, e -> fillForm(contract));
            btnEdit.setParent(actionCell);

            Button btnDelete = new Button("Xóa");
            btnDelete.setSclass("px-3 py-1 rounded-lg me-2 bg-red-500 text-white hover:bg-red-600 !important");
            btnDelete.addEventListener(Events.ON_CLICK, e -> {
                Messagebox.show("Bạn có chắc chắn muốn xóa hợp đồng: " + contract.getName() + "?",
                        "Xác nhận", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, evt -> {
                            if (Messagebox.ON_YES.equals(evt.getName())) {
                                // TODO: Xử lý xóa hợp đồng
                                contractDAO.deleteContract(contract.getId());
                                Messagebox.show(" Xoá thành công ");
                                Executions.sendRedirect(null);

                            }
                        });
            });
            btnDelete.setParent(actionCell);
            actionCell.setParent(item);
        });

        // Sự kiện chọn dòng → fill form
        contractListbox.addEventListener(Events.ON_SELECT, event -> {
            if (contractListbox.getSelectedItem() != null) {
                Contract selected = contractListbox.getSelectedItem().getValue();
                currentIdContract = selected.getId();
                fillForm(selected);
            }
        });

        // Khởi tạo upload file
        uploadFile();
        applyComboxBoxUser();

        // Nút refresh form
        btnRefreshForm.addEventListener(Events.ON_CLICK, e -> cleanForm());

        // Nút lưu hợp đồng
        btnSaveForm.addEventListener(Events.ON_CLICK, event -> {
            createOrUpdateContract();
            Messagebox.show("Update hợp đồng thành công");
        });

        btnCancel.addEventListener(Events.ON_CLICK, event -> {
            cleanForm();
        });

        // Search box
        searchBox.addEventListener(Events.ON_CHANGE, event -> {
            searchTmp = searchBox.getValue();
            contracts = contractDAO.getAllContractWithFilter(searchTmp, statusTmp, roleTmp, scopeTmp, typeTmp);
            ListModelList<Contract> newModel = new ListModelList<>(contracts);
            contractListbox.setModel(newModel);
        });

        //cb status filter
        cbStatusFilter.addEventListener(Events.ON_CHANGE, event -> {
            statusTmp = cbStatusFilter.getSelectedItem() != null
                    ? cbStatusFilter.getSelectedItem().getValue()
                    : null;

            contracts = contractDAO.getAllContractWithFilter(searchTmp, statusTmp, roleTmp, scopeTmp, typeTmp);
            contractListbox.setModel(new ListModelList<>(contracts));
        });

        //cb scopy filter
        cbScopeFilter.addEventListener(Events.ON_CHANGE, event -> {
            scopeTmp = cbScopeFilter.getSelectedItem() != null
                    ? cbScopeFilter.getSelectedItem().getValue()
                    : null;
            contracts = contractDAO.getAllContractWithFilter(searchTmp, statusTmp, roleTmp, scopeTmp, typeTmp);
            contractListbox.setModel(new ListModelList<>(contracts));
        });

        //cb TypeFilter
        cbTypeFilter.addEventListener(Events.ON_CHANGE, event -> {
            typeTmp = cbTypeFilter.getSelectedItem() != null
                    ? cbTypeFilter.getSelectedItem().getValue()
                    : null;
            contracts = contractDAO.getAllContractWithFilter(searchTmp, statusTmp, roleTmp, scopeTmp, typeTmp);
            contractListbox.setModel(new ListModelList<>(contracts));
        });

        //cb role filter
        cbRoleFilter.addEventListener(Events.ON_CHANGE, event -> {
            roleTmp = cbRoleFilter.getSelectedItem() != null
                    ? cbRoleFilter.getSelectedItem().getValue()
                    : null;

            contracts = contractDAO.getAllContractWithFilter(searchTmp, statusTmp, roleTmp, scopeTmp, typeTmp);
            contractListbox.setModel(new ListModelList<>(contracts));
        });
    }

    // Đổ dữ liệu vào form khi sửa
    private void fillForm(Contract c) {
        txtNumberContract.setValue(c.getNumberContract());
        txtNameContract.setValue(c.getName());
        cbStatus.setValue(c.getStatus());
        cbScope.setValue(c.getContractScope());
        dbStartDate.setValue(c.getStartDate());
        dbEndDate.setValue(c.getEndDate());
        txtEmailA.setValue(c.getEmailA());
        txtPhoneA.setValue(c.getPhoneA());
        txtEmailB.setValue(c.getEmailB());
        txtPhoneB.setValue(c.getPhoneB());

        cbApprover.setItemRenderer((ComboitemRenderer<Staff>) (item, data, index) -> {
            item.setLabel(data.getName());  // Hiển thị name
            item.setValue(data.getId());    // Giá trị thực là ID
        });

        cbContractType.setValue(c.getContractType());
        cbPaymentMethod.setValue(c.getPaymentMethod());
        fileURL = c.getFile_data();

        cbApprover.setSelectedIndex(-1);
        for (Comboitem item : cbApprover.getItems()) {
            if (item.getValue().equals(c.getStaffID())) {
                cbApprover.setSelectedItem(item);
                break;
            }
        }
    }

    // Reset form
    private void cleanForm() {
        currentIdContract = null;
        txtNumberContract.setValue("");
        txtNameContract.setValue("");
        txtEmailA.setValue("");
        txtPhoneA.setValue("");
        txtEmailB.setValue("");
        txtPhoneB.setValue("");

        cbStatus.setSelectedIndex(-1);
        cbScope.setSelectedIndex(-1);
        cbContractType.setSelectedIndex(-1);
        cbPaymentMethod.setSelectedIndex(-1);

        dbStartDate.setValue(null);
        dbEndDate.setValue(null);

        fileURL = null;
    }

    // Upload file và trả về URL public
    private void uploadFile() throws Exception {
        fileContract.addEventListener(Events.ON_UPLOAD, event -> {
            UploadEvent ue = (UploadEvent) event;
            Media media = ue.getMedia();
            if (media == null) return;

            try {
                // Khởi tạo Storage từ FirebaseConfig
                Storage storage = StorageOptions.newBuilder()
                        .setCredentials(FirebaseConfig.getCredentials())
                        .build()
                        .getService();

                String bucketName = FirebaseApp.getInstance().getOptions().getStorageBucket();
                String fileName = "contracts/" + System.currentTimeMillis() + "_" + media.getName();

                BlobId blobId = BlobId.of(bucketName, fileName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                        .setContentType(media.getContentType())
                        .build();

                // Upload file lên bucket
                Blob blob = storage.create(blobInfo, media.getByteData());

                // Set quyền public cho file vừa upload
                blob.createAcl(com.google.cloud.storage.Acl.of(
                        com.google.cloud.storage.Acl.User.ofAllUsers(),
                        com.google.cloud.storage.Acl.Role.READER
                ));

                // URL public
                String fileUrl = "https://storage.googleapis.com/" + bucketName + "/" + fileName;

                // Gán vào form và hiển thị thông báo
                fileContract.setLabel(media.getName());
                fileURL = fileUrl;

                Messagebox.show("Upload thành công!\n" + fileUrl, "Thông báo", Messagebox.OK, Messagebox.INFORMATION);

            } catch (Exception ex) {
                ex.printStackTrace();
                Messagebox.show("Upload thất bại: " + ex.getMessage(), "Lỗi", Messagebox.OK, Messagebox.ERROR);
            }
        });
    }

    // Mapping form → Contract
    private Contract mapingContract() {
        Contract contract = new Contract();
        contract.setNumberContract(txtNumberContract.getValue());
        contract.setName(txtNameContract.getValue());
        contract.setStatus(cbStatus.getValue());
        contract.setStartDate(dbStartDate.getValue());
        contract.setContractScope(cbScope.getValue());
        contract.setEmailA(txtEmailA.getValue());
        contract.setPhoneA(txtPhoneA.getValue());
        contract.setFile_data(fileURL);

        if (cbApprover.getSelectedItem() != null) {
            contract.setStaffID((Integer) cbApprover.getSelectedItem().getValue());
        }

        contract.setContractType(cbContractType.getValue());
        contract.setPaymentMethod(cbPaymentMethod.getValue());
        contract.setEndDate(dbEndDate.getValue());
        contract.setEmailB(txtEmailB.getValue());
        contract.setPhoneB(txtPhoneB.getValue());

        return contract;
    }

    // Lưu hoặc update hợp đồng
    private void createOrUpdateContract() {
        Contract contract = mapingContract();
        if (currentIdContract == null) {
            contractDAO.creatNewContract(contract);
        } else {
            contract.setId(currentIdContract);
            contractDAO.updateContract(contract);
        }
        Executions.sendRedirect(null);
    }

    private void applyComboxBoxUser() {
        List<Staff> staffList = staffDAO.getAllStaff();
        cbApprover.getItems().clear();
        for (Staff staff : staffList) {
            Comboitem comboitem = new Comboitem(staff.getName());
            comboitem.setValue(staff.getId());
            comboitem.setParent(cbApprover);
        }
    }
}
