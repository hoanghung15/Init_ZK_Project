package org.example.testdemozul.controller;

import org.example.testdemozul.dao.ContractDAO;
import org.example.testdemozul.model.Contract;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import java.text.SimpleDateFormat;
import java.util.List;


public class DetailPageContractController extends SelectorComposer<Component> {
    @Wire
    private Listbox contractListbox;

    private ContractDAO contractDAO = new ContractDAO();
    private List<Contract> contracts;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        contracts = contractDAO.getAllContracts();
        ListModelList<Contract> model = new ListModelList<>(contracts);
        contractListbox.setModel(model);
        contractListbox.setItemRenderer((Listitem item, Contract contract, int index) -> {
            // STT
            new Listcell(String.valueOf(index + 1)).setParent(item);
            // ID
            new Listcell(String.valueOf(contract.getId())).setParent(item);
            // Số hiệu
            new Listcell(contract.getNumberContract()).setParent(item);
            // Tên hợp đồng
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

            // --- Cột file_data (hiển thị nút "Xem") ---
            Listcell fileCell = new Listcell();
            A viewLink = new A("Xem file hợp đồng"); // Tạo link chữ "Xem"
            viewLink.setSclass("text-blue-500 underline cursor-pointer hover:underline");
            viewLink.setStyle("text-decoration: underline;");
            viewLink.addEventListener(Events.ON_CLICK, event -> {
                // Nếu bạn lưu path file PDF trong DB
                String filePath = contract.getFile_data();
                if (filePath != null && !filePath.isEmpty()) {
                    // Mở file PDF trong tab mới
                    Executions.getCurrent().sendRedirect(filePath, "_blank");
                } else {
                    Messagebox.show("Không tìm thấy file đính kèm!", "Thông báo", Messagebox.OK, Messagebox.EXCLAMATION);
                }
            });
            viewLink.setParent(fileCell);
            fileCell.setParent(item);

            // Ngày bắt đầu
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String startDateStr = contract.getStartDate() != null ? sdf.format(contract.getStartDate()) : "";
            new Listcell(startDateStr).setParent(item);

            // Ngày kết thúc
            String endDateStr = contract.getEndDate() != null ? sdf.format(contract.getEndDate()) : "";
            new Listcell(endDateStr).setParent(item);

            // Action
            Listcell actionCell = new Listcell();

            // Nút Sửa
            Button btnEdit = new Button("Sửa");
            btnEdit.setSclass("bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded-lg me-2");
            btnEdit.addEventListener(Events.ON_CLICK, e -> {
                Messagebox.show("Bạn muốn sửa hợp đồng: " + contract.getName());
                // TODO: Mở form sửa hoặc popup
            });
            btnEdit.setParent(actionCell);

            // Nút Xóa
            Button btnDelete = new Button("Xóa");
            btnDelete.setSclass("bg-red-500 hover:bg-red-600 text-white px-3 py-1 bg-danger rounded-lg");
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

    }
}
