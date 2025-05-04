// Help system functionality
class HelpSystem {
    constructor() {
        this.addHelpButton();
        this.addHelpModal();
        this.initializeEventListeners();
    }

    addHelpButton() {
        const button = document.createElement('button');
        button.id = 'helpButton';
        button.className = 'fixed top-4 right-4 z-50 bg-blue-600 text-white rounded-full w-12 h-12 flex items-center justify-center shadow-lg hover:bg-blue-700 transition-colors';
        button.innerHTML = '<i class="fas fa-question text-xl"></i>';
        document.body.appendChild(button);
    }

    addHelpModal() {
        const modal = document.createElement('div');
        modal.id = 'helpModal';
        modal.className = 'modal';
        modal.innerHTML = `
            <div class="modal-content max-w-4xl">
                <div class="flex justify-between items-center mb-6">
                    <h3 class="text-2xl font-semibold">Help Guide</h3>
                    <button id="closeHelpButton" class="text-gray-500 hover:text-gray-700">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div id="helpContent" class="prose max-h-[70vh] overflow-y-auto p-4">
                    Loading help content...
                </div>
            </div>
        `;
        document.body.appendChild(modal);
    }

    initializeEventListeners() {
        document.getElementById('helpButton').addEventListener('click', () => this.showHelp());
        document.getElementById('closeHelpButton').addEventListener('click', () => this.closeHelp());
    }

    async showHelp() {
        const helpModal = document.getElementById('helpModal');
        const helpContent = document.getElementById('helpContent');
        
        try {
            const response = await fetch('HELP.md');
            const markdown = await response.text();
            helpContent.innerHTML = this.markdownToHtml(markdown);
        } catch (error) {
            helpContent.innerHTML = `
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
                    Error loading help content. Please try again later.
                </div>
            `;
            console.error('Error loading help content:', error);
        }
        
        helpModal.classList.add('active');
    }

    closeHelp() {
        document.getElementById('helpModal').classList.remove('active');
    }

    markdownToHtml(markdown) {
        return markdown
            // Headers
            .replace(/^# (.*$)/gm, '<h1 class="text-3xl font-bold mb-6 mt-8">$1</h1>')
            .replace(/^## (.*$)/gm, '<h2 class="text-2xl font-semibold mb-4 mt-6">$1</h2>')
            .replace(/^### (.*$)/gm, '<h3 class="text-xl font-semibold mb-3 mt-4">$1</h3>')
            // Lists
            .replace(/^\d\. (.+)/gm, '<li class="ml-6 mb-2">$1</li>')
            .replace(/^- (.+)/gm, '<li class="ml-6 mb-2">• $1</li>')
            // Code blocks
            .replace(/```([\s\S]*?)```/gm, '<pre class="bg-gray-100 p-4 rounded-lg my-4 overflow-x-auto"><code>$1</code></pre>')
            // Inline code
            .replace(/`([^`]+)`/g, '<code class="bg-gray-100 px-2 py-1 rounded">$1</code>')
            // Paragraphs
            .replace(/^\n(?!<[hl]|<li)/gm, '<p class="mb-4">')
            // Lists wrapper
            .replace(/<li class="ml-6 mb-2">/g, '<ul class="list-none mb-4"><li class="ml-6 mb-2">')
            .replace(/<\/li>\n(?!<li)/g, '</li></ul>\n');
    }
}

// Initialize help system when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.helpSystem = new HelpSystem();
});
