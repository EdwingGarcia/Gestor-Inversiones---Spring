package com.edwinggarcia.Inversiones.controller.dto;

import java.util.List;

public class TendenciasDTO {
    private List<String> topEstrategias;
    private List<String> topActivos;
    private String topTipoActivo;

    public TendenciasDTO() {
    }

    public List<String> getTopActivos() {
        return topActivos;
    }

    public void setTopActivos(List<String> topActivos) {
        this.topActivos = topActivos;
    }

    public List<String> getTopEstrategias() {
        return topEstrategias;
    }

    public void setTopEstrategias(List<String> topEstrategias) {
        this.topEstrategias = topEstrategias;
    }

    public String getTopTipoActivo() {
        return topTipoActivo;
    }

    public void setTopTipoActivo(String topTipoActivo) {
        this.topTipoActivo = topTipoActivo;
    }
}
