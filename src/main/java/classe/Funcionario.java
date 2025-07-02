package classe;

/**
 * Representa um funcionário da empresa, estendendo a classe Pessoa.
 */
public class Funcionario extends Pessoa {
    // Código único do funcionário (formato FXXX)
    private String matricula;
    // Departamento ao qual o funcionário pertence
    private String departamento;

    /**
     * Construtor padrão para a classe Funcionario.
     */
    public Funcionario() {
        super(); // Chama o construtor padrão da superclasse
    }

    /**
     * Construtor com parâmetros para inicializar um Funcionario.
     *
     * @param id           Identificador único (chave estrangeira referenciando Pessoa)
     * @param nome         Nome completo do funcionário
     * @param email        E-mail do funcionário
     * @param matricula    Código único do funcionário (ex. F001)
     * @param departamento Departamento do funcionário
     */
    public Funcionario(int id, String nome, String email, String matricula, String departamento) {
        super(id, nome, email); // Inicializa atributos da superclasse
        // Usa setters para validação
        setMatricula(matricula);
        setDepartamento(departamento);
    }

    /**
     * Obtém a matrícula do funcionário.
     *
     * @return a matrícula do funcionário
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Define a matrícula do funcionário.
     *
     * @param matricula a matrícula do funcionário (formato: F seguido de três dígitos)
     * @throws IllegalArgumentException se a matrícula for inválida
     */
    public void setMatricula(String matricula) {
        // Valida o formato da matrícula (Regra de Negócio 4)
        if (matricula == null || !matricula.matches("F\\d{3}")) {
            throw new IllegalArgumentException("Matrícula deve seguir o formato F seguido de três dígitos (ex. F001).");
        }
        this.matricula = matricula;
    }

    /**
     * Obtém o departamento do funcionário.
     *
     * @return o departamento do funcionário
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Define o departamento do funcionário.
     *
     * @param departamento o departamento do funcionário
     * @throws IllegalArgumentException se o departamento for nulo ou vazio
     */
    public void setDepartamento(String departamento) {
        // Valida se o departamento é válido (Regra de Negócio 4)
        if (departamento == null || departamento.trim().isEmpty()) {
            throw new IllegalArgumentException("Departamento não pode ser nulo ou vazio.");
        }
        this.departamento = departamento;
    }

    /**
     * Retorna uma representação em string do funcionário.
     *
     * @return string com os detalhes do funcionário
     */
    @Override
    public String toString() {
        return "Funcionario [id=" + getId() + ", nome=" + getNome() + ", email=" + getEmail() +
               ", matricula=" + matricula + ", departamento=" + departamento + "]";
    }
}