package org.example.testdemozul.controller;

import org.example.testdemozul.dao.ContractDAO;
import org.example.testdemozul.model.Contract;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
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
            new Listcell(contract.getStatus()).setParent(item);

            // --- Cột file_data (hiển thị nút "Xem") ---
            Listcell fileCell = new Listcell();
            A viewLink = new A("Xem file hợp đồng"); // Tạo link chữ "Xem"
            viewLink.setSclass("text-blue-500 underline cursor-pointer");

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
        });

    }
}
