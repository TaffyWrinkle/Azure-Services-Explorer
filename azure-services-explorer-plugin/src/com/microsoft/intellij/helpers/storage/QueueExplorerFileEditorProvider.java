/**
 * Copyright (c) Microsoft Corporation
 * <p/>
 * All rights reserved.
 * <p/>
 * MIT License
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.microsoft.intellij.helpers.storage;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.microsoft.intellij.helpers.IDEHelperImpl;
import com.microsoft.tooling.msservices.model.storage.ClientStorageAccount;
import com.microsoft.tooling.msservices.model.storage.Queue;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class QueueExplorerFileEditorProvider implements FileEditorProvider, DumbAware {
    public static Key<Queue> QUEUE_KEY = new Key<Queue>("queue");

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        ClientStorageAccount storageAccount = virtualFile.getUserData(IDEHelperImpl.STORAGE_KEY);
        Queue queue = virtualFile.getUserData(QUEUE_KEY);

        return (storageAccount != null && queue != null);
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        QueueFileEditor queueFileEditor = new QueueFileEditor();

        ClientStorageAccount storageAccount = virtualFile.getUserData(IDEHelperImpl.STORAGE_KEY);
        Queue queue = virtualFile.getUserData(QUEUE_KEY);

        queueFileEditor.setQueue(queue);
        queueFileEditor.setStorageAccount(storageAccount);
        queueFileEditor.setProject(project);

        queueFileEditor.fillGrid();

        return queueFileEditor;
    }

    @Override
    public void disposeEditor(@NotNull FileEditor fileEditor) {
        Disposer.dispose(fileEditor);
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element element, @NotNull Project project, @NotNull VirtualFile virtualFile) {
        return FileEditorState.INSTANCE;
    }

    @Override
    public void writeState(@NotNull FileEditorState fileEditorState, @NotNull Project project, @NotNull Element element) {
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "Azure-Storage-Queue-Editor";
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
