import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class FilaTandem {
    List<FilaSimples> tandem;
    
    public FilaTandem(List<FilaSimples> tandem) {
        this.tandem = tandem;
    }

    public List<FilaSimples> getFilaTandem() {
        return tandem;
    }

    public int getPerda() {
        int aux = 0;
        for(FilaSimples f:tandem) {
            aux+=f.getPerda();
        }   
        return aux;
    }

    public List<BigDecimal> getEstados(){
        List<BigDecimal> todosEstados = new ArrayList<BigDecimal>();
        for(FilaSimples f:tandem) {
            todosEstados.addAll(f.getEstados());
        }
        return todosEstados;
    }
}