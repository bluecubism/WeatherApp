package com.example.weatherapp.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// real time data

data class SwobRealtimeAPIData( // root
    val type: String,
    val features: List<SwobFeature>,
    val numberMatched: Long,
    val numberReturned: Long,
    val links: List<SwobLink>,
    val timeStamp: String,
)

data class SwobFeature(
    val id: String,
    val type: String,
    val geometry: SwobGeometry,
    val properties: SwobProperties,
)

data class SwobGeometry(
    val type: String,
    val coordinates: List<Double>, // x,y,z coordinates
)

// uom = unit of measure
data class SwobLink(
    val type: String,
    val rel: String,
    val title: String,
    val href: String,
)

data class SwobProperties(
    val dataset: String,
    val id: String,
    val url: String,
    @SerializedName("date_tm-uom")
    val dateTmUom: String,
    @SerializedName("date_tm-value")
    val dateTmValue: String,
    @SerializedName("stn_nam-uom")
    val stnNamUom: String,
    @SerializedName("stn_nam-value")
    val stnNamValue: String,
    @SerializedName("tc_id-uom")
    val tcIdUom: String?,
    @SerializedName("tc_id-value")
    val tcIdValue: String?,
    @SerializedName("wmo_synop_id-uom")
    val wmoSynopIdUom: String?,
    @SerializedName("wmo_synop_id-value")
    val wmoSynopIdValue: String?,
    @SerializedName("data_pvdr-uom")
    val dataPvdrUom: String?,
    @SerializedName("data_pvdr-value")
    val dataPvdrValue: String?,
    @SerializedName("msc_id-uom")
    val mscIdUom: String?,
    @SerializedName("msc_id-value")
    val mscIdValue: String?,
    @SerializedName("clim_id-uom")
    val climIdUom: String?,
    @SerializedName("clim_id-value")
    val climIdValue: String?,
    @SerializedName("obs_date_tm")
    val obsDateTm: String?,
    @SerializedName("processed_date_tm")
    val processedDateTm: String?,
    @SerializedName("data_avail_pst1mt")
    val dataAvailPst1mt: Long,
    @SerializedName("data_avail_pst1mt-uom")
    val dataAvailPst1mtUom: String?,
    @SerializedName("data_avail_pst1mt-qa")
    val dataAvailPst1mtQa: Long,
    @SerializedName("max_batry_volt_pst1mt")
    val maxBatryVoltPst1mt: Float,
    @SerializedName("max_batry_volt_pst1mt-uom")
    val maxBatryVoltPst1mtUom: String?,
    @SerializedName("max_batry_volt_pst1mt-qa")
    val maxBatryVoltPst1mtQa: Long,
    @SerializedName("min_batry_volt_pst1mt")
    val minBatryVoltPst1mt: Float,
    @SerializedName("min_batry_volt_pst1mt-uom")
    val minBatryVoltPst1mtUom: String?,
    @SerializedName("min_batry_volt_pst1mt-qa")
    val minBatryVoltPst1mtQa: Long,
    @SerializedName("logr_panl_temp")
    val logrPanlTemp: Float,
    @SerializedName("logr_panl_temp-uom")
    val logrPanlTempUom: String?,
    @SerializedName("logr_panl_temp-qa")
    val logrPanlTempQa: Long,
    @SerializedName("air_temp")
    val airTemp: Float,
    @SerializedName("air_temp-uom")
    val airTempUom: String?,
    @SerializedName("air_temp-qa")
    val airTempQa: Long,
    @SerializedName("rel_hum")
    val relHum: Long,
    @SerializedName("rel_hum-uom")
    val relHumUom: String?,
    @SerializedName("rel_hum-qa")
    val relHumQa: Long,
    @SerializedName("max_rel_hum_pst1mt")
    val maxRelHumPst1mt: Long,
    @SerializedName("max_rel_hum_pst1mt-uom")
    val maxRelHumPst1mtUom: String?,
    @SerializedName("max_rel_hum_pst1mt-qa")
    val maxRelHumPst1mtQa: Long,
    @SerializedName("min_rel_hum_pst1mt")
    val minRelHumPst1mt: Long,
    @SerializedName("min_rel_hum_pst1mt-uom")
    val minRelHumPst1mtUom: String?,
    @SerializedName("min_rel_hum_pst1mt-qa")
    val minRelHumPst1mtQa: Long,
    @SerializedName("avg_wnd_spd_pcpn_gag_pst1mt")
    val avgWndSpdPcpnGagPst1mt: Float,
    @SerializedName("avg_wnd_spd_pcpn_gag_pst1mt-uom")
    val avgWndSpdPcpnGagPst1mtUom: String?,
    @SerializedName("avg_wnd_spd_pcpn_gag_pst1mt-qa")
    val avgWndSpdPcpnGagPst1mtQa: Long,
    @SerializedName("stn_pres")
    val stnPres: Float,
    @SerializedName("stn_pres-uom")
    val stnPresUom: String?,
    @SerializedName("stn_pres-qa")
    val stnPresQa: Long,
    @SerializedName("avg_cum_pcpn_gag_wt_fltrd_pst5mts")
    val avgCumPcpnGagWtFltrdPst5mts: Float,
    @SerializedName("avg_cum_pcpn_gag_wt_fltrd_pst5mts-uom")
    val avgCumPcpnGagWtFltrdPst5mtsUom: String?,
    @SerializedName("avg_cum_pcpn_gag_wt_fltrd_pst5mts-qa")
    val avgCumPcpnGagWtFltrdPst5mtsQa: Long,
    @SerializedName("snw_dpth")
    val snwDpth: Long,
    @SerializedName("snw_dpth-uom")
    val snwDpthUom: String?,
    @SerializedName("snw_dpth-qa")
    val snwDpthQa: Long,
    @SerializedName("avg_snw_dpth_pst5mts")
    val avgSnwDpthPst5mts: Long,
    @SerializedName("avg_snw_dpth_pst5mts-uom")
    val avgSnwDpthPst5mtsUom: String?,
    @SerializedName("avg_snw_dpth_pst5mts-qa")
    val avgSnwDpthPst5mtsQa: Long,
    @SerializedName("rnfl_amt_pst1mt")
    val rnflAmtPst1mt: Float,
    @SerializedName("rnfl_amt_pst1mt-uom")
    val rnflAmtPst1mtUom: String?,
    @SerializedName("rnfl_amt_pst1mt-qa")
    val rnflAmtPst1mtQa: Long,
    @SerializedName("min_air_temp_pst1hr")
    val minAirTempPst1hr: Float,
    @SerializedName("min_air_temp_pst1hr-uom")
    val minAirTempPst1hrUom: String?,
    @SerializedName("min_air_temp_pst1hr-data_flag-uom")
    val minAirTempPst1hrDataFlagUom: String?,
    @SerializedName("min_air_temp_pst1hr-data_flag-code_src")
    val minAirTempPst1hrDataFlagCodeSrc: String?,
    @SerializedName("min_air_temp_pst1hr-data_flag-value")
    val minAirTempPst1hrDataFlagValue: Long,
    @SerializedName("max_air_temp_pst1hr")
    val maxAirTempPst1hr: Float,
    @SerializedName("max_air_temp_pst1hr-uom")
    val maxAirTempPst1hrUom: String?,
    @SerializedName("max_air_temp_pst1hr-data_flag-uom")
    val maxAirTempPst1hrDataFlagUom: String?,
    @SerializedName("max_air_temp_pst1hr-data_flag-code_src")
    val maxAirTempPst1hrDataFlagCodeSrc: String?,
    @SerializedName("max_air_temp_pst1hr-data_flag-value")
    val maxAirTempPst1hrDataFlagValue: Long,
    @SerializedName("pcpn_amt_snc_top_of_hr")
    val pcpnAmtSncTopOfHr: Float,
    @SerializedName("pcpn_amt_snc_top_of_hr-uom")
    val pcpnAmtSncTopOfHrUom: String?,
    @SerializedName("pcpn_amt_snc_top_of_hr-data_flag-uom")
    val pcpnAmtSncTopOfHrDataFlagUom: String?,
    @SerializedName("pcpn_amt_snc_top_of_hr-data_flag-code_src")
    val pcpnAmtSncTopOfHrDataFlagCodeSrc: String?,
    @SerializedName("pcpn_amt_snc_top_of_hr-data_flag-value")
    val pcpnAmtSncTopOfHrDataFlagValue: Long,
    @SerializedName("avg_air_temp_pst1hr")
    val avgAirTempPst1hr: Float,
    @SerializedName("avg_air_temp_pst1hr-uom")
    val avgAirTempPst1hrUom: String?,
    @SerializedName("avg_air_temp_pst1hr-data_flag-uom")
    val avgAirTempPst1hrDataFlagUom: String?,
    @SerializedName("avg_air_temp_pst1hr-data_flag-code_src")
    val avgAirTempPst1hrDataFlagCodeSrc: String?,
    @SerializedName("avg_air_temp_pst1hr-data_flag-value")
    val avgAirTempPst1hrDataFlagValue: Long,
    @SerializedName("min_batry_volt_pst1hr")
    val minBatryVoltPst1hr: Float,
    @SerializedName("min_batry_volt_pst1hr-uom")
    val minBatryVoltPst1hrUom: String?,
    @SerializedName("min_batry_volt_pst1hr-data_flag-uom")
    val minBatryVoltPst1hrDataFlagUom: String?,
    @SerializedName("min_batry_volt_pst1hr-data_flag-code_src")
    val minBatryVoltPst1hrDataFlagCodeSrc: String?,
    @SerializedName("min_batry_volt_pst1hr-data_flag-value")
    val minBatryVoltPst1hrDataFlagValue: Long,
    @SerializedName("max_batry_volt_pst1hr")
    val maxBatryVoltPst1hr: Float,
    @SerializedName("max_batry_volt_pst1hr-uom")
    val maxBatryVoltPst1hrUom: String?,
    @SerializedName("max_batry_volt_pst1hr-data_flag-uom")
    val maxBatryVoltPst1hrDataFlagUom: String?,
    @SerializedName("max_batry_volt_pst1hr-data_flag-code_src")
    val maxBatryVoltPst1hrDataFlagCodeSrc: String?,
    @SerializedName("max_batry_volt_pst1hr-data_flag-value")
    val maxBatryVoltPst1hrDataFlagValue: Long,
    @SerializedName("data_avail_pst1hr")
    val dataAvailPst1hr: Long,
    @SerializedName("data_avail_pst1hr-uom")
    val dataAvailPst1hrUom: String?,
    @SerializedName("data_avail_pst1hr-data_flag-uom")
    val dataAvailPst1hrDataFlagUom: String?,
    @SerializedName("data_avail_pst1hr-data_flag-code_src")
    val dataAvailPst1hrDataFlagCodeSrc: String?,
    @SerializedName("data_avail_pst1hr-data_flag-value")
    val dataAvailPst1hrDataFlagValue: Long,
    @SerializedName("min_rel_hum_pst1hr")
    val minRelHumPst1hr: Long,
    @SerializedName("min_rel_hum_pst1hr-uom")
    val minRelHumPst1hrUom: String?,
    @SerializedName("min_rel_hum_pst1hr-data_flag-uom")
    val minRelHumPst1hrDataFlagUom: String?,
    @SerializedName("min_rel_hum_pst1hr-data_flag-code_src")
    val minRelHumPst1hrDataFlagCodeSrc: String?,
    @SerializedName("min_rel_hum_pst1hr-data_flag-value")
    val minRelHumPst1hrDataFlagValue: Long,
    @SerializedName("avg_rel_hum_pst1hr")
    val avgRelHumPst1hr: Long,
    @SerializedName("avg_rel_hum_pst1hr-uom")
    val avgRelHumPst1hrUom: String?,
    @SerializedName("avg_rel_hum_pst1hr-data_flag-uom")
    val avgRelHumPst1hrDataFlagUom: String?,
    @SerializedName("avg_rel_hum_pst1hr-data_flag-code_src")
    val avgRelHumPst1hrDataFlagCodeSrc: String?,
    @SerializedName("avg_rel_hum_pst1hr-data_flag-value")
    val avgRelHumPst1hrDataFlagValue: Long,
    @SerializedName("pcpn_amt_pst10mts")
    val pcpnAmtPst10mts: Float,
    @SerializedName("pcpn_amt_pst10mts-uom")
    val pcpnAmtPst10mtsUom: String?,
    @SerializedName("pcpn_amt_pst10mts-data_flag-uom")
    val pcpnAmtPst10mtsDataFlagUom: String?,
    @SerializedName("pcpn_amt_pst10mts-data_flag-code_src")
    val pcpnAmtPst10mtsDataFlagCodeSrc: String?,
    @SerializedName("pcpn_amt_pst10mts-data_flag-value")
    val pcpnAmtPst10mtsDataFlagValue: Long,
    @SerializedName("avg_wnd_spd_pcpn_gag_pst10mts")
    val avgWndSpdPcpnGagPst10mts: Float,
    @SerializedName("avg_wnd_spd_pcpn_gag_pst10mts-uom")
    val avgWndSpdPcpnGagPst10mtsUom: String?,
    @SerializedName("avg_wnd_spd_pcpn_gag_pst10mts-data_flag-uom")
    val avgWndSpdPcpnGagPst10mtsDataFlagUom: String?,
    @SerializedName("avg_wnd_spd_pcpn_gag_pst10mts-data_flag-code_src")
    val avgWndSpdPcpnGagPst10mtsDataFlagCodeSrc: String?,
    @SerializedName("avg_wnd_spd_pcpn_gag_pst10mts-data_flag-value")
    val avgWndSpdPcpnGagPst10mtsDataFlagValue: Long,
    @SerializedName("max_rel_hum_pst1hr")
    val maxRelHumPst1hr: Long,
    @SerializedName("max_rel_hum_pst1hr-uom")
    val maxRelHumPst1hrUom: String?,
    @SerializedName("max_rel_hum_pst1hr-data_flag-uom")
    val maxRelHumPst1hrDataFlagUom: String?,
    @SerializedName("max_rel_hum_pst1hr-data_flag-code_src")
    val maxRelHumPst1hrDataFlagCodeSrc: String?,
    @SerializedName("max_rel_hum_pst1hr-data_flag-value")
    val maxRelHumPst1hrDataFlagValue: Long,
    @SerializedName("rnfl_amt_pst1hr")
    val rnflAmtPst1hr: Float,
    @SerializedName("rnfl_amt_pst1hr-uom")
    val rnflAmtPst1hrUom: String?,
    @SerializedName("rnfl_amt_pst1hr-data_flag-uom")
    val rnflAmtPst1hrDataFlagUom: String?,
    @SerializedName("rnfl_amt_pst1hr-data_flag-code_src")
    val rnflAmtPst1hrDataFlagCodeSrc: String?,
    @SerializedName("rnfl_amt_pst1hr-data_flag-value")
    val rnflAmtPst1hrDataFlagValue: Long,
    @SerializedName("dwpt_temp")
    val dwptTemp: Float,
    @SerializedName("dwpt_temp-uom")
    val dwptTempUom: String?,
    @SerializedName("dwpt_temp-data_flag-uom")
    val dwptTempDataFlagUom: String?,
    @SerializedName("dwpt_temp-data_flag-code_src")
    val dwptTempDataFlagCodeSrc: String?,
    @SerializedName("dwpt_temp-data_flag-value")
    val dwptTempDataFlagValue: Long,
    @SerializedName("avg_dwpt_temp_pst1hr")
    val avgDwptTempPst1hr: Float,
    @SerializedName("avg_dwpt_temp_pst1hr-uom")
    val avgDwptTempPst1hrUom: String?,
    @SerializedName("avg_dwpt_temp_pst1hr-data_flag-uom")
    val avgDwptTempPst1hrDataFlagUom: String?,
    @SerializedName("avg_dwpt_temp_pst1hr-data_flag-code_src")
    val avgDwptTempPst1hrDataFlagCodeSrc: String?,
    @SerializedName("avg_dwpt_temp_pst1hr-data_flag-value")
    val avgDwptTempPst1hrDataFlagValue: Long,
    @SerializedName("wetblb_temp")
    val wetblbTemp: Float,
    @SerializedName("wetblb_temp-uom")
    val wetblbTempUom: String?,
    @SerializedName("wetblb_temp-data_flag-uom")
    val wetblbTempDataFlagUom: String?,
    @SerializedName("wetblb_temp-data_flag-code_src")
    val wetblbTempDataFlagCodeSrc: String?,
    @SerializedName("wetblb_temp-data_flag-value")
    val wetblbTempDataFlagValue: Long,
    @SerializedName("pcpn_amt_pst1hr")
    val pcpnAmtPst1hr: Float,
    @SerializedName("pcpn_amt_pst1hr-uom")
    val pcpnAmtPst1hrUom: String?,
    @SerializedName("pcpn_amt_pst1hr-data_flag-uom")
    val pcpnAmtPst1hrDataFlagUom: String?,
    @SerializedName("pcpn_amt_pst1hr-data_flag-code_src")
    val pcpnAmtPst1hrDataFlagCodeSrc: String?,
    @SerializedName("pcpn_amt_pst1hr-data_flag-value")
    val pcpnAmtPst1hrDataFlagValue: Long,
    @SerializedName("air_temp_1")
    val airTemp1: Float,
    @SerializedName("air_temp_1-uom")
    val airTemp1Uom: String?,
    @SerializedName("air_temp_2")
    val airTemp2: Float,
    @SerializedName("air_temp_2-uom")
    val airTemp2Uom: String?,
    @SerializedName("air_temp_3")
    val airTemp3: Float,
    @SerializedName("air_temp_3-uom")
    val airTemp3Uom: String?,
    @SerializedName("_is-minutely_obs-value")
    val IsMinutelyObsValue: Boolean,
    @SerializedName("avg_wnd_dir_10m_pst10mts")
    val avgWndDir10mPst10mts: Long,
    @SerializedName("avg_wnd_dir_10m_pst10mts-uom")
    val avgWndDir10mPst10mtsUom: String?,
    @SerializedName("max_wnd_gst_spd_10m_pst10mts")
    val maxWndGstSpd10mPst10mts: Float,
    @SerializedName("max_wnd_gst_spd_10m_pst10mts-uom")
    val maxWndGstSpd10mPst10mtsUom: String?,
    @SerializedName("tot_cld_amt")
    var totCldAmt : Long,
    @SerializedName("tot_cld_amt-uom")
    var totCldAmtUom : String?,
    @SerializedName("vis")
    var vis : Float,
    @SerializedName("vis-uom")
    var visUom : String?
)