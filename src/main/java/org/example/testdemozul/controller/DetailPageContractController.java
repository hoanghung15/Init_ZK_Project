package org.example.testdemozul.controller;

import org.example.testdemozul.dao.ContractDAO;
import org.example.testdemozul.model.Contract;
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
    private Textbox txtNumberContract, txtNameContract, txtEmailA, txtPhoneA, txtEmailB, txtPhoneB, txtApprover;
    @Wire
    private Combobox cbStatus, cbScope, cbContractType, cbPaymentMethod;
    @Wire
    private Datebox dbStartDate, dbEndDate;
    @Wire
    private Fileupload fileContract;

    @Wire
    private Button btnRefreshForm;

    private ContractDAO contractDAO = new ContractDAO();
    private List<Contract> contracts;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        contracts = contractDAO.getAllContracts();
        ListModelList<Contract> model = new ListModelList<>(contracts);
        contractListbox.setModel(model);

        contractListbox.setItemRenderer((Listitem item, Contract contract, int index) -> {
            // Render từng cell
            item.setValue(contract);
            new Listcell(String.valueOf(index + 1)).setParent(item);
            new Listcell(String.valueOf(contract.getId())).setParent(item);
            new Listcell(contract.getNumberContract()).setParent(item);
            new Listcell(contract.getName()).setParent(item);

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

            // Ngày bắt đầu
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
                            }
                        });
            });
            btnDelete.setParent(actionCell);
            actionCell.setParent(item);
        });

        // Sự kiện chọn dòng
        contractListbox.addEventListener(Events.ON_SELECT, event -> {
            if (contractListbox.getSelectedItem() != null) {
                Contract selected = contractListbox.getSelectedItem().getValue();
                fillForm(selected);
            }
        });
        uploadFile();

        btnRefreshForm.addEventListener(Events.ON_CLICK, e -> {
            cleanForm();
        });
    }

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
        txtApprover.setValue(String.valueOf(c.getStaffID()));
        cbContractType.setValue(c.getContractType());
        cbPaymentMethod.setValue(c.getPaymentMethod());
    }

    private void cleanForm() {
        // Textbox
        txtNumberContract.setValue("");
        txtNameContract.setValue("");
        txtEmailA.setValue("");
        txtPhoneA.setValue("");
        txtEmailB.setValue("");
        txtPhoneB.setValue("");
        txtApprover.setValue("");

        // Combobox
        cbStatus.setSelectedIndex(-1);        // Bỏ chọn
        cbScope.setSelectedIndex(-1);
        cbContractType.setSelectedIndex(-1);
        cbPaymentMethod.setSelectedIndex(-1);

        // Datebox
        dbStartDate.setValue(null);
        dbEndDate.setValue(null);

//        fileContract.detach();
//        fileContract = new Fileupload();
//        fileContract.setParent(getSelf()); // Nếu muốn render lại
    }

    private void uploadFile(){
        fileContract.addEventListener(Events.ON_UPLOAD, event -> {
            UploadEvent ue = (UploadEvent) event;
            Media media = ue.getMedia();
            if (media != null) {
              fileContract.setLabel(media.getName());
            }
        });
    }

}
