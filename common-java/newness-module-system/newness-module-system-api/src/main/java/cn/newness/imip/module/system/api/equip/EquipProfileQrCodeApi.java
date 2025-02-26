package cn.newness.imip.module.system.api.equip;

/**
 * @author machuran
 * @date 8/30/2024
 * @time 10:13 AM
 * @Description
 */
public interface EquipProfileQrCodeApi {
    String createAndUploadQrCode(String equipProfileId,String equipName,String equipCode,String text);
}
